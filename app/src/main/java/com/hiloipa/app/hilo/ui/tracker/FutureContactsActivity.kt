package com.hiloipa.app.hilo.ui.tracker

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.FutureContactsAdapter
import com.hiloipa.app.hilo.adapter.GoalTrackerAdapter
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.contacts.ContactDetailsActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_future_contacts.*

class FutureContactsActivity : AppCompatActivity(), GoalTrackerAdapter.ContactClickListener {

    lateinit var adapter: FutureContactsAdapter
    lateinit var backLogsAdapter: FutureContactsAdapter
    lateinit var goalType: GoalType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_contacts)
        toolbar.setNavigationOnClickListener { finish() }

        // get goal type from intent
        val intGoalType = intent.extras.getInt("goalType")
        goalType = GoalType.fromInt(intGoalType)
        // set toolbar title according to goal type
        toolbarTitle.text = getString(goalType.title())
        // set list header text according to goal type
        if (goalType != GoalType.reach_outs)
            futureDescriptionLabel.text = getString(goalType.description())
        else futureDescriptionLabel.visibility = View.GONE
        // create adapter and setup recycler view
        adapter = FutureContactsAdapter(this, goalType)
        adapter.delegate = this
        backLogsAdapter = FutureContactsAdapter(this, goalType)
        backLogsAdapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        backLogsRecyclerView.adapter = backLogsAdapter
        backLogsRecyclerView.layoutManager = LinearLayoutManager(this)
        backLogsRecyclerView.isNestedScrollingEnabled = false
        // get contacts from server
        when (goalType) {
            GoalType.reach_outs -> this.getFutureReachOutContacts()
            GoalType.follow_ups -> this.getFutureFollowUpContacts()
            GoalType.team_reach_outs -> this.getFutureTeamReachOutContacts()
        }
    }


    private fun getFutureFollowUpContacts() {
        val dialog = showLoading()
        val request = StandardRequest()
        request.type = GoalType.follow_ups.apiValue()
        HiloApp.api().getFutureFollowUps(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FutureFollowUps> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            adapter.updateAdapterData<FollowUpContact>(data.futureFollowups)
                            if (data.backlogs.isNotEmpty()) {
                                backLogsHeader.visibility = View.VISIBLE
                                backLogsAdapter.updateAdapterData<FollowUpContact>(data.backlogs)
                            }
                        }
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    finish()
                })
    }

    private fun getFutureTeamReachOutContacts() {
        val dialog = showLoading()
        val request = StandardRequest()
        request.type = GoalType.team_reach_outs.apiValue()
        HiloApp.api().getFutureTeamReachOuts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FutureTeamReachOuts> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            adapter.updateAdapterData<TeamReachOutContact>(data.futureFollowups)
                            if (data.backlogs.isNotEmpty()) {
                                backLogsHeader.visibility = View.VISIBLE
                                backLogsAdapter.updateAdapterData<TeamReachOutContact>(data.backlogs)
                            }
                        }
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    finish()
                })
    }

    private fun getFutureReachOutContacts() {
        val dialog = showLoading()
        val request = StandardRequest()
        request.type = GoalType.reach_outs.apiValue()
        HiloApp.api().getFutureReachOutContacts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ArrayList<ReachOutContact>> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            adapter.updateAdapterData<ReachOutContact>(data)
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    finish()
                })
    }

    private fun completeContact(contact: Contact) {
        val dialog = showLoading()
        val request = StandardRequest()
        request.type = goalType.apiValue()
        request.contactId = "${contact.id}"
        HiloApp.api().showCompleteOption(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<CompleteOption> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            when (goalType) {
                                GoalType.follow_ups, GoalType.reach_outs -> this.showCompleteReachOutDialog(data)
                                GoalType.team_reach_outs -> this.showCompleteTeamReachOutDialog(data)
                            }
                        }
                    } else {
                        showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    override fun onCompleteClicked(contact: Contact, position: Int) {
        completeContact(contact)
    }

    override fun onDeleteClicked(contact: Contact, position: Int) {

    }

    override fun onContactClicked(contact: Contact, position: Int) {
        val intent = Intent(this, ContactDetailsActivity::class.java)
        val extras = Bundle()
        extras.putParcelable("contact", contact)
        intent.putExtras(extras)
        this.startActivity(intent)
    }

    private fun showCompleteReachOutDialog(option: CompleteOption) {
        val dialogView = layoutInflater.inflate(R.layout.alert_complete_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val title: RalewayTextView = dialogView.findViewById(R.id.completeReachOutTile)
        val leadTempBtn: RalewayButton = dialogView.findViewById(R.id.leadTempBtn)
        val leadTempSpinner: Spinner = dialogView.findViewById(R.id.leadTempSpinner)
        val pipelinePos: RalewayButton = dialogView.findViewById(R.id.updatePipelinePositionBtn)
        val pipelineSpinner: Spinner = dialogView.findViewById(R.id.updatePipelinePositionSpinner)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val scheduleNextFollowUp: RalewayButton = dialogView.findViewById(R.id.scheduleNextFollowUpBtn)
        val contactType: RalewayButton = dialogView.findViewById(R.id.contactTypeBtn)
        val contactTypeSpinner: Spinner = dialogView.findViewById(R.id.contactTypeSpinner)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        // set dialog data
        title.text = option.contactName
        // setup lead temp spinner and button
        var isFromUser = false
        var selectedLeadTemp = option.temps.firstOrNull { it.value == "${option.leadTemp}" }
        leadTempBtn.text = selectedLeadTemp?.text
        val tempsTitle = mutableListOf<String>()
        option.temps.forEach { tempsTitle.add(it.text) }
        leadTempSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, tempsTitle)
        leadTempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    val temp = option.temps[position]
                    if (temp.value.isEmpty()) selectedLeadTemp = null
                    else selectedLeadTemp = temp
                    leadTempBtn.text = temp.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        leadTempBtn.setOnClickListener {
            isFromUser = true
            leadTempSpinner.performClick()
        }

        pipelinePos.text = option.pipelines.firstOrNull { it.value == "${option.pipeline}" }?.text

        contactType.text = option.contactTypes.firstOrNull { it.value == option.contactType }?.text

        logReachOutType.text = option.reachOutTypes.firstOrNull { it.value == option.type }?.text

        val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCompleteTeamReachOutDialog(completeOption: CompleteOption) {
        val dialogView = layoutInflater.inflate(R.layout.alert_complete_team_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
