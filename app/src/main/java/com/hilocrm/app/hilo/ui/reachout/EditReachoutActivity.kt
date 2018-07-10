package com.hilocrm.app.hilo.ui.reachout

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.requests.SaveReachOutLogRequest
import com.hilocrm.app.hilo.models.responses.*
import com.hilocrm.app.hilo.ui.FragmentSearchContacts
import com.hilocrm.app.hilo.ui.base.BaseActivity
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_reachout.*
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class EditReachoutActivity : BaseActivity(), FragmentSearchContacts.SearchDelegate {

    val reachOutTypes = buildReachOutTypes()
    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm aaa", Locale.getDefault())

    companion object {
        val logKey = "com.hiloipa.app.hilo.ui.reachout.LOG"
    }

    var reachOutLog: ReachOutLog? = null
    var contactId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reachout)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        if (intent.extras != null)
            contactId = intent.extras.getString("contactId", null)

        if (intent.extras != null && intent.extras.containsKey(logKey)) {
            reachOutLog = intent.extras.getParcelable(logKey)
            // setup views with the data from existent log
            // set selected log type
            reachoutsTypeButton.text = reachOutLog!!.typeName
            reachoutsTypeButton.tag = "${reachOutLog!!.historyType}"
            // set log date
            dateBtn.text = dateFormat.format(reachOutLog!!.sortTime)
            dateBtn.tag = reachOutLog!!.sortTime
            // set log time
            timeBtn.text = timeFormat.format(reachOutLog!!.sortTime)
            timeBtn.tag = reachOutLog!!.sortTime
            // set log description
            val description = reachOutLog!!.description
            if (description.isNotEmpty()) {
                val data = Base64.decode(description, Base64.DEFAULT)
                reachoutDescriptionField.setText(String(data, Charset.forName("utf-8")).replace("\\n", "\n"))
            } else
                reachoutDescriptionField.setText(description)
            // set next follow up date
            nextFollowUpBtn.text = dateFormat.format(reachOutLog!!.sortTime)
            nextFollowUpBtn.tag = reachOutLog!!.sortTime
            contactField.text = reachOutLog!!.contactName
            contactField.tag = "${reachOutLog!!.contactId}"
        }

        if (contactId != null) {
            contactField.visibility = View.GONE
            contactField.tag = contactId
        }

        // reach out type spinner and button
        val types = mutableListOf<String>()
        var isTypeFromUser = false
        reachOutTypes.forEach { types.add(it.text) }
        reachTypeSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, types)
        reachTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTypeFromUser) {
                    val type = reachOutTypes[position]
                    reachoutsTypeButton.text = type.text
                    reachoutsTypeButton.tag = type.value
                    isTypeFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        reachoutsTypeButton.setOnClickListener {
            isTypeFromUser = true
            reachTypeSpinner.performClick()
        }

        // date button
        val calendar = Calendar.getInstance()
        if (reachOutLog == null)
            dateBtn.text = dateFormat.format(Date())
        dateBtn.setOnClickListener {
            if (reachOutLog != null)
                calendar.time = reachOutLog!!.sortTime
            else calendar.time = Date()

            val picker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                dateBtn.text = dateFormat.format(newDate)
                dateBtn.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            picker.show()
        }

        // time button
        if (reachOutLog == null)
            timeBtn.text = timeFormat.format(Date())
        timeBtn.setOnClickListener {
            if (reachOutLog != null)
                calendar.time = reachOutLog!!.sortTime
            else calendar.time = Date()

            val picker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newTime = Date(calendar.timeInMillis)
                timeBtn.text = timeFormat.format(newTime)
                timeBtn.tag = newTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            picker.show()
        }

        // contact
        contactField.setOnClickListener {
            FragmentSearchContacts.newInstance(this)
                    .show(fragmentManager, "SearchContact")
        }

        // next follow up
        nextFollowUpBtn.setOnClickListener {
            if (reachOutLog != null)
                calendar.time = reachOutLog!!.sortTime
            else calendar.time = Date()

            val picker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                nextFollowUpBtn.text = dateFormat.format(newDate)
                nextFollowUpBtn.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            picker.show()
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        saveBtn.setOnClickListener { saveReachLog() }
    }

    override fun onContactSelected(contact: Contact) {
        contactField.text = contact.name
        contactField.tag = "${contact.id}"
    }

    private fun saveReachLog() {
        var reachOutId = "0"
        if (reachOutLog != null) reachOutId = "${reachOutLog!!.historyID}"

        val reachOutType = reachoutsTypeButton.tag as String? ?: ""
        if (reachOutType.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_reach_out_type),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val date = dateBtn.text.toString()
        if (date.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, dateBtn.hint),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val time = timeBtn.text.toString()
        if (time.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, timeBtn.hint),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val description = reachoutDescriptionField.text.toString()
        if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_description),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val contactId = contactField.tag as String? ?: ""
        if (contactId.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_selected_contacts),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val request = SaveReachOutLogRequest()
        request.date = date
        request.time = time
        request.description = description
        request.contactId = contactId
        request.reachOutId = reachOutId
        request.reachType = reachOutType
        request.type = "Contact"

        val loading = showLoading()
        HiloApp.api().saveReachOutLog(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        setResult(Activity.RESULT_OK)
                        Toast.makeText(this, response.data, Toast.LENGTH_SHORT).show()
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun buildReachOutTypes(): List<DropDownValue> = listOf<DropDownValue>(
            DropDownValue("Other", "113", false),
            DropDownValue("Phone Call", "101", false),
            DropDownValue("Text", "102", false),
            DropDownValue("Email", "103", false),
            DropDownValue("In Person Meeting", "104", false),
            DropDownValue("LinkedIn", "105", false),
            DropDownValue("Facebook Messenger", "106", false),
            DropDownValue("Facebook Post", "107", false),
            DropDownValue("Facebook Live", "108", false),
            DropDownValue("Instagram", "109", false),
            DropDownValue("Twitter", "110", false),
            DropDownValue("Periscope", "111", false),
            DropDownValue("Landing Page", "112", false)
    )
}
