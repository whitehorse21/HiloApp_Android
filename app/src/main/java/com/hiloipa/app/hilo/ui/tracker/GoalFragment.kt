package com.hiloipa.app.hilo.ui.tracker


import android.app.DatePickerDialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.GoalTrackerAdapter
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.requests.CompleteReachRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.FragmentSearchContacts
import com.hiloipa.app.hilo.ui.contacts.details.ContactDetailsActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_goal.*
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class GoalFragment : Fragment(), GoalTrackerAdapter.ContactClickListener, FragmentSearchGoalContact.SearchGoalDelegate {

    lateinit var adapter: GoalTrackerAdapter
    lateinit var goalType: GoalType

    companion object {
        fun newInstance(data: GoalTrackerResponse, goalType: GoalType, period: GoalDurationObjc): GoalFragment {
            val args = Bundle()
            args.putParcelable("data", data)
            args.putInt("goalType", goalType.toInt())
            args.putParcelable("duration", period)
            val fragment = GoalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_goal, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filter = IntentFilter("contacts_ready")
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, filter)
        // init goal duration getting it's value from arguments
        val duration = arguments.getParcelable<GoalDurationObjc>("duration")
        goalType = GoalType.fromInt(arguments.getInt("goalType"))
        val data = arguments.getParcelable<GoalTrackerResponse>("data")
        // init contacts adapter
        adapter = GoalTrackerAdapter(activity)
        adapter.delegate = this
        this.updateFragmentData(data, duration)
        // set adapter to recycler view and setup the recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        // set buttons listeners
        showFutureBtn.setOnClickListener {
            val intent = Intent(activity, FutureContactsActivity::class.java)
            val extras = Bundle()
            extras.putInt("goalType", goalType.toInt())
            intent.putExtras(extras)
            activity.startActivity(intent)
        }
    }

    fun updateFragmentData(data: GoalTrackerResponse = arguments.getParcelable("data"),
                           period: GoalDurationObjc = arguments.getParcelable("duration")) {
        // refresh adapter
        adapter.data = data
        adapter.goalType = goalType
        adapter.notifyDataSetChanged()
        // refresh graph data when period changed
        // change graph title text to contain selected duration
        when (adapter.goalType) {
            GoalType.reach_outs ->
                reachoutsTypeLabel.text = getString(R.string.reach_outs_s, period.name)

            GoalType.follow_ups ->
                reachoutsTypeLabel.text = getString(R.string.follow_ups_s, period.name)

            GoalType.team_reach_outs ->
                reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, period.name)
        }

        // update graph details name
        targetTitleLabel.text = getString(R.string.s_ntarget, period.value)
        completedTitleLabel.text = getString(R.string.s_ncompleted, period.value)
        percentageTitleLabel.text = getString(R.string.s_npercentage, period.value)

        // setup graph progress bar and texts
        when (adapter.goalType) {
            GoalType.reach_outs -> {
                reachoutsTypeLabel.text = getString(R.string.reach_outs_s, period.name)
                selectedTypeTitleLabel.text = getString(R.string.new_reach_outs)
                targetLabel.text = "${data.reachOuts.reachOutsGraphData.target}"
                completedLabel.text = "${data.reachOuts.reachOutsGraphData.completed}"
                percentageLabel.text = "%${data.reachOuts.reachOutsGraphData.percentage}"
                progressBar.color = resources.getColor(R.color.colorPrimary)
                progressBar.progress = data.reachOuts.reachOutsGraphData.percentage.toFloat()
            }
            GoalType.follow_ups -> {
                reachoutsTypeLabel.text = getString(R.string.follow_ups_s, period.name)
                selectedTypeTitleLabel.text = getString(R.string.follow_ups)
                targetLabel.text = "${data.followUps.followUpsGraphData.target}"
                completedLabel.text = "${data.followUps.followUpsGraphData.completed}"
                percentageLabel.text = "%${data.followUps.followUpsGraphData.percentage}"
                progressBar.color = resources.getColor(R.color.colorGreen)
                progressBar.progress = data.followUps.followUpsGraphData.percentage.toFloat()
            }
            GoalType.team_reach_outs -> {
                reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, period.name)
                selectedTypeTitleLabel.text = getString(R.string.team_reach_outs)
                targetLabel.text = "${data.teamReachOuts.teamReachoutsGraphData.target}"
                completedLabel.text = "${data.teamReachOuts.teamReachoutsGraphData.completed}"
                percentageLabel.text = "%${data.teamReachOuts.teamReachoutsGraphData.percentage}"
                progressBar.color = resources.getColor(R.color.colorBlue)
                progressBar.progress = data.teamReachOuts.teamReachoutsGraphData.percentage.toFloat()
            }
        }

        progressLabel.text = "${progressBar.progress.toInt()}%"
    }

    override fun onCompleteClicked(contact: Contact, position: Int) {
        completeContact(contact)
    }

    override fun onDeleteClicked(contact: Contact, position: Int) {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.hilo))
                .setMessage(getString(R.string.remove_goal_contact, getString(goalType.title())))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity.showLoading()
                    val request = StandardRequest()
                    request.contactId = "${contact.id}"
                    request.type = goalType.apiValue()
                    HiloApp.api().removeGoalTrackerContact(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    activity.sendBroadcast(Intent("update_tracker"))
                                } else {
                                    activity.showExplanation(message = response.message)
                                }
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        dialog.show()
    }

    override fun onContactClicked(contact: Contact, position: Int) {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        val extras = Bundle()
        extras.putString(ContactDetailsActivity.contactIdKey, "${contact.id}")
        intent.putExtras(extras)
        activity.startActivity(intent)
    }

    override fun didWantToSearchContact() {
        FragmentSearchGoalContact.newInstance(this, goalType.searchUrl())
                .show(activity.fragmentManager, "SearchContacts")
    }

    override fun onContactSelected(contact: Contact) {
        val request = StandardRequest()
        request.type = goalType.apiValue()
        request.contactId = "${contact.id}"
        val loading = activity.showLoading()
        HiloApp.api().addGoalTrackerContact(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent("update_tracker"))
                    } else {
                        activity.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when(intent.action) {
                "contacts_ready" -> {
                    val contacts: ArrayList<DetailedContact> = intent.extras.getParcelableArrayList("contacts")
                    adapter.contacts = contacts
                }
            }
        }
    }

    private fun completeContact(contact: Contact) {
        val dialog = activity.showLoading()
        val request = StandardRequest()
        request.type = goalType.apiValue()
        request.contactId = "${contact.id}"
        if (goalType == GoalType.team_reach_outs) {
            HiloApp.api().getTeamContactId(contact.id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ body: ResponseBody ->
                        request.contactId = body.string()
                        showCompleteOptions(request, dialog)
                    }, { error: Throwable ->
                        error.printStackTrace()
                        activity.showExplanation(message = error.localizedMessage)
                    })
        } else showCompleteOptions(request, dialog)
    }

    private fun showCompleteOptions(request: StandardRequest, dialog: AlertDialog) {
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
                        activity.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private fun showCompleteReachOutDialog(option: CompleteOption) {
        val dialogView = activity.layoutInflater.inflate(R.layout.alert_complete_reach_out, null)
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
        var isTempFromUser = false
        var selectedLeadTemp = option.temps.firstOrNull { it.value == "${option.leadTemp}" && it.value.isNotEmpty() }
        leadTempBtn.text = selectedLeadTemp?.text
        val tempsTitle = mutableListOf<String>()
        option.temps.forEach { tempsTitle.add(it.text) }
        leadTempSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, tempsTitle)
        leadTempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTempFromUser) {
                    val temp = option.temps[position]
                    if (temp.value.isEmpty()) selectedLeadTemp = null
                    else selectedLeadTemp = temp
                    leadTempBtn.text = temp.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        leadTempBtn.setOnClickListener {
            isTempFromUser = true
            leadTempSpinner.performClick()
        }
        // setup pipeline position spinner and buttn
        var isPipelineFromUser = false
        var selectedPipeline = option.pipelines.firstOrNull { it.value == "${option.pipeline}" && it.value.isNotEmpty() }
        pipelinePos.text = option.pipelines.firstOrNull { it.value == "${option.pipeline}" }?.text
        val pipelinesTitle = mutableListOf<String>()
        option.pipelines.forEach { pipelinesTitle.add(it.text) }
        pipelineSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, pipelinesTitle)
        pipelineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isPipelineFromUser) {
                    val pipeline = option.pipelines[position]
                    if (pipeline.value.isEmpty()) selectedPipeline = null
                    else selectedPipeline = pipeline
                    pipelinePos.text = pipeline.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        pipelinePos.setOnClickListener {
            isPipelineFromUser = true
            pipelineSpinner.performClick()
        }
        // setup contact type spinner and button
        var isContactTypeFromUser = false
        var selectedContactType = option.contactTypes.firstOrNull { it.value == option.contactType && it.value.isNotEmpty() }
        contactType.text = option.contactTypes.firstOrNull { it.value == option.contactType }?.text
        val typesName = mutableListOf<String>()
        option.contactTypes.forEach { typesName.add(it.text) }
        contactTypeSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, typesName)
        contactTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isContactTypeFromUser) {
                    val type = option.contactTypes[position]
                    if (type.value.isEmpty()) selectedContactType = null
                    else selectedContactType = type
                    contactType.text = type.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        contactType.setOnClickListener {
            isContactTypeFromUser = true
            contactTypeSpinner.performClick()
        }
        // setup reach out type spinner and button
        var isLogFromUser = false
        var selectedLogType = option.reachOutTypes.firstOrNull { it.value == option.reachOutType && it.value.isNotEmpty() }
        logReachOutType.text = option.reachOutTypes.firstOrNull { it.value == option.type }?.text
        val logTypes = mutableListOf<String>()
        option.reachOutTypes.forEach { logTypes.add(it.text) }
        logReachOutTypeSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, logTypes)
        logReachOutTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isLogFromUser) {
                    val log = option.reachOutTypes[position]
                    if (log.value.isEmpty()) selectedLogType = null
                    else selectedLogType = log
                    logReachOutType.text = log.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        logReachOutType.setOnClickListener {
            isLogFromUser = true
            logReachOutTypeSpinner.performClick()
        }
        // setup next follow up button
        var nextFollowUp: Date? = null
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val datePickerDialog = DatePickerDialog(activity, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val now = Date()
                nextFollowUp = Date(calendar.timeInMillis)
                if (nextFollowUp!! < now) {
                    activity.showExplanation(title = getString(R.string.wrong_date),
                            message = getString(R.string.wrong_date_message))
                    nextFollowUp = null
                } else
                    scheduleNextFollowUp.text = dateFormat.format(nextFollowUp!!)
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        scheduleNextFollowUp.setOnClickListener { datePickerDialog.show() }

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        completeBtn.setOnClickListener {
            val request = CompleteReachRequest()
            request.contactId = "${option.contactId}"
            request.goalType = goalType.apiValue()
            if (selectedLeadTemp == null) {
                Toast.makeText(activity, getString(R.string.lead_temp),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            request.leadTemp = selectedLeadTemp!!.value
            if (selectedPipeline == null) {
                Toast.makeText(activity, getString(R.string.update_pipeline_position),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            request.pipeline = selectedPipeline!!.value
            if (selectedLogType == null) {
                Toast.makeText(activity, getString(R.string.log_reach_out_type),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            request.reachOutType = selectedLogType!!.value
            request.reachOutComments = logReachOutComment.text.toString()
            if (nextFollowUp != null)
                request.nextFollowUp = dateFormat.format(nextFollowUp)
            if (selectedContactType != null)
                request.contactType = selectedContactType!!.value
            val loading = activity.showLoading()
            HiloApp.api().completeGoal(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.status.isSuccess()) {
                            dialog.dismiss()
                            LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent("update_tracker"))
                        } else {
                            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }, { error: Throwable ->
                        loading.dismiss()
                        error.printStackTrace()
                        Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
        }

        dialog.show()
    }

    private fun showCompleteTeamReachOutDialog(option: CompleteOption) {
        val dialogView = layoutInflater.inflate(R.layout.alert_complete_team_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        var isLogFromUser = false
        var selectedLogType = option.reachOutTypes.firstOrNull { it.value == option.reachOutType && it.value.isNotEmpty() }
        logReachOutType.text = option.reachOutTypes.firstOrNull { it.value == option.type }?.text
        val logTypes = mutableListOf<String>()
        option.reachOutTypes.forEach { logTypes.add(it.text) }
        logReachOutTypeSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, logTypes)
        logReachOutTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isLogFromUser) {
                    val log = option.reachOutTypes[position]
                    if (log.value.isEmpty()) selectedLogType = null
                    else selectedLogType = log
                    logReachOutType.text = log.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        logReachOutType.setOnClickListener {
            isLogFromUser = true
            logReachOutTypeSpinner.performClick()
        }

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        completeBtn.setOnClickListener {
            val request = CompleteReachRequest()
            request.contactId = "${option.contactId}"
            request.goalType = goalType.apiValue()

            if (selectedLogType == null) {
                Toast.makeText(activity, getString(R.string.log_reach_out_type),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            request.reachOutType = selectedLogType!!.value
            request.reachOutComments = logReachOutComment.text.toString()

            val loading = activity.showLoading()
            HiloApp.api().completeGoal(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.status.isSuccess()) {
                            dialog.dismiss()
                            activity.sendBroadcast(Intent("update_tracker"))
                        } else {
                            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }, { error: Throwable ->
                        loading.dismiss()
                        error.printStackTrace()
                        Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    })
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver)
    }
}
