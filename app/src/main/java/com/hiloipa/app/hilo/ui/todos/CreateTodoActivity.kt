package com.hiloipa.app.hilo.ui.todos

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TagsSpinnerAdapter
import com.hiloipa.app.hilo.models.Tag
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.FragmentSearchContacts
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_todo.*
import kotlinx.android.synthetic.main.layout_event_time.*
import kotlinx.android.synthetic.main.layout_todo_time.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateTodoActivity : AppCompatActivity(), FragmentSearchContacts.SearchDelegate {

    var item: ToDo? = null
    lateinit var todoType: TodoType
    var actions: ArrayList<ActionItem> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
    val timeFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)
    val sessionFormat = SimpleDateFormat("aaa", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        // setup spinners and date buttons
        setupAmPmSpinner()
        setupPrioritySpinner()
        setupDueDateBtn()
        setupEndTimeBtn()
        setupEventAmPmSpinner()
        getContactActions()
        // get to do type from intent
        todoType = TodoType.fromInt(intent.extras.getInt("type"))

        // get item from intent
        when (todoType) {
            TodoType.goal -> item = intent.extras.getParcelable("data") as Goal?
            TodoType.action -> item = intent.extras.getParcelable("data") as Action?
            TodoType.need -> item = intent.extras.getParcelable("data") as TeamNeed?
            TodoType.event -> item = intent.extras.getParcelable("data") as Event?
        }

        todoTimeLayout.visibility = if (todoType == TodoType.event) View.GONE else View.VISIBLE
        eventTimeLayout.visibility = if (todoType == TodoType.event) View.VISIBLE else View.GONE
        actionsLayout.visibility = if (todoType == TodoType.goal) View.VISIBLE else View.GONE
        locationLabel.visibility = if (todoType == TodoType.event) View.VISIBLE else View.GONE

        todoTitleField.hint = getString(R.string.s_title, todoType.title())
        toolbarTitle.text = todoType.title()
        allDayCheckBox.text = getString(R.string.all_day_s, todoType.title())
        headerButton.text = getString(R.string.add_s, todoType.title())

        contactField.setOnClickListener {
            FragmentSearchContacts.newInstance(this)
                    .show(fragmentManager, "SearchContacts")
        }

        if (item != null) {
            headerButton.text = getString(R.string.edit_s, todoType.title())
            todoTitleField.setText(item!!.name)
            allDayCheckBox.isChecked = item!!.allDay ?: false
            contactField.text = item!!.contactName
            contactField.tag = "${item!!.contactId}"
            todoDescriptionField.setText(item!!.details)


            when (todoType) {
                TodoType.goal -> {
                    val dueDate = item!!.dueDate
                    priorityBtn.text
                    if (dueDate != null) {
                        dueDateBtn.text = dateFormat.format(dueDate)
                        endTimeBtn.text = timeFormat.format(dueDate)
                        amPmBtn.text = sessionFormat.format(dueDate)
                    }
                    val actions = (item as Goal).actions
                    val builder = StringBuilder()
                    val regex = "(,$)*".toRegex()
                    actions.forEach { builder.append(it.name).append(",") }
                    actionsBtn.text = regex.replace(builder.toString(), "")
                    builder.setLength(0)
                    actions.forEach { builder.append(it.id).append(",") }
                    actionsBtn.tag = regex.replace(builder.toString(), "")
                    builder.setLength(0)
                }
                TodoType.action, TodoType.need -> {
                    val dueDate = item!!.dueDate
                    priorityBtn.text
                    if (dueDate != null) {
                        dueDateBtn.text = dateFormat.format(dueDate)
                        endTimeBtn.text = timeFormat.format(dueDate)
                        amPmBtn.text = sessionFormat.format(dueDate)
                    }
                }
                TodoType.event -> {
                    val event = item as Event
                    // setup start date
                    eventStartDate.text = dateFormat.format(event.startDate)
                    eventStartTime.text = timeFormat.format(event.startDate)
                    eventStartTimeAmPm.text = sessionFormat.format(event.startDate)

                    // setup end date
                    eventEndDate.text = dateFormat.format(event.endDate)
                    eventEndTime.text = timeFormat.format(event.endDate)
                    eventEndTimeAmPm.text = sessionFormat.format(event.endDate)

                    // location
                    locationLabel.setText(event.location)

                    // event type
                    eventTypeBtn.text = event.eventType
                }
            }
        }
    }

    override fun onContactSelected(contact: DetailedContact) {
        contactField.text = "${contact.firstName} ${contact.lastName}"
        contactField.tag = "${contact.id}"
    }

    private fun setupDueDateBtn() {
        val calendar = Calendar.getInstance()
        if (item != null && item!!.dueDate != null) calendar.time = item!!.dueDate!!
        dueDateBtn.setOnClickListener {
            val alert = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                dueDateBtn.text = dateFormat.format(newDate)
                dueDateBtn.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            alert.show()
        }
    }

    private fun setupEndTimeBtn() {
        val calendar = Calendar.getInstance()
        if (item != null && item!!.dueDate != null) calendar.time = item!!.dueDate!!
        endTimeBtn.setOnClickListener {
            val alert = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newDate = Date(calendar.timeInMillis)
                endTimeBtn.text = timeFormat.format(newDate)
                endTimeBtn.tag = newDate
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            alert.show()
        }
    }

    private fun setupAmPmSpinner() {
        var isFromUser = false
        val sessions = mutableListOf<String>("AM", "PM")
        amPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        amPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    amPmBtn.text = sessions[position]
                    amPmBtn.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        amPmBtn.setOnClickListener {
            isFromUser = true
            amPmSpinner.performClick()
        }
    }

    private fun setupPrioritySpinner() {
        var isFromUser = false
        val priorities = mutableListOf<String>("", "Low", "Medium", "Hight")
        prioritySpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, priorities)
        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    priorityBtn.text = priorities[position]
                    priorityBtn.tag = "${position}"
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        priorityBtn.setOnClickListener {
            isFromUser = true
            prioritySpinner.performClick()
        }
    }

    private fun setupEventAmPmSpinner() {
        var isFromUser = false
        val sessions = mutableListOf<String>("AM", "PM")
        eventStartTimeAmPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        eventStartTimeAmPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    eventStartTimeAmPm.text = sessions[position]
                    eventStartTimeAmPm.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        eventStartTimeAmPm.setOnClickListener {
            isFromUser = true
            eventStartTimeAmPmSpinner.performClick()
        }

        eventEndTimeAmPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        eventEndTimeAmPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    eventEndTimeAmPm.text = sessions[position]
                    eventEndTimeAmPm.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        eventEndTimeAmPm.setOnClickListener {
            isFromUser = true
            eventEndTimeAmPmSpinner.performClick()
        }
    }

    private fun setupActionsSpinner() {
        val actions = arrayListOf<Tag>()
        val goal = item as Goal?
        if (goal != null) {
            this.actions.forEach { action: ActionItem ->
                action.isSelected = goal.actions.firstOrNull { it.id == action.id } != null
            }
        }
        this.actions.forEach { actions.add(Tag(it.name, it.isSelected)) }
        val adapter = TagsSpinnerAdapter(actions)
        adapter.delegate = object : TagsSpinnerAdapter.TagSpinnerDelegate {
            override fun didClickOnTag(tag: Tag, position: Int) {
                val action = this@CreateTodoActivity.actions[position]
                var currentText = actionsBtn.text.toString()
                var currentTag = actionsBtn.tag as String? ?: ""
                if (currentText.isNotEmpty() && tag.isSelected) {
                    currentText = "$currentText,${action.name}"
                    currentTag = "$currentTag,${action.id}"
                } else if (tag.isSelected) {
                    currentText = action.name
                    currentTag = "${action.id}"
                } else {
                    val regex = "(,)*(${tag.name})(,$)*".toRegex()
                    currentText = regex.replace(currentText, "")
                    currentTag = regex.replace(currentTag, "")
                    if (currentText.startsWith(",")) currentText = "^,".toRegex()
                            .replace(currentText, "")
                    if (currentTag.startsWith(",")) currentTag = "^,".toRegex()
                            .replace(currentText, "")
                }

                actionsBtn.text = currentText
                actionsBtn.tag = currentTag
            }
        }
        actionsSpinner.adapter = adapter
        actionsBtn.setOnClickListener { actionsSpinner.performClick() }
    }

    private fun getContactActions() {
        val loading = showLoading()
        val request = StandardRequest()
        request.contactId = item?.contactId ?: "0"
        HiloApp.api().getContactActions(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ActionDropDown> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            this.actions.addAll(data.actionItems)
                        setupActionsSpinner()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })

    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ActionDropDown(@JsonProperty("ActionDropdown") val actionItems: ArrayList<ActionItem>)

@JsonIgnoreProperties(ignoreUnknown = true)
class ActionItem(@JsonProperty("id") val id: Int,
                 @JsonProperty("name") val name: String) {
    var isSelected = false
}
