package com.hiloipa.app.hilo.ui.tracker


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.hiloipa.app.hilo.R
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
import kotlinx.android.synthetic.main.fragment_goal.*


/**
 * A simple [Fragment] subclass.
 */
class GoalFragment : Fragment(), GoalTrackerAdapter.ContactClickListener {

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
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar)
            }
            GoalType.follow_ups -> {
                reachoutsTypeLabel.text = getString(R.string.follow_ups_s, period.name)
                selectedTypeTitleLabel.text = getString(R.string.follow_ups)
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar_green)
            }
            GoalType.team_reach_outs -> {
                reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, period.name)
                selectedTypeTitleLabel.text = getString(R.string.team_reach_outs)
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar_blue)
            }
        }

        // setup TextViews with their text
        when (adapter.goalType) {
            GoalType.reach_outs -> {
                targetLabel.text = "${data.reachOuts.reachOutsGraphData.target}"
                completedLabel.text = "${data.reachOuts.reachOutsGraphData.completed}"
                percentageLabel.text = "%${data.reachOuts.reachOutsGraphData.percentage}"
                progressBar.progress = data.reachOuts.reachOutsGraphData.percentage
                progressBarPercentageLabel.text = "${progressBar.progress}%"
            }
            GoalType.follow_ups -> {
                targetLabel.text = "${data.followUps.followUpsGraphData.target}"
                completedLabel.text = "${data.followUps.followUpsGraphData.completed}"
                percentageLabel.text = "%${data.followUps.followUpsGraphData.percentage}"
                progressBar.progress = data.followUps.followUpsGraphData.percentage
                progressBarPercentageLabel.text = "${progressBar.progress}%"
            }
            GoalType.team_reach_outs -> {
                targetLabel.text = "${data.teamReachOuts.teamReachoutsGraphData.target}"
                completedLabel.text = "${data.teamReachOuts.teamReachoutsGraphData.completed}"
                percentageLabel.text = "%${data.teamReachOuts.teamReachoutsGraphData.percentage}"
                progressBar.progress = data.teamReachOuts.teamReachoutsGraphData.percentage
                progressBarPercentageLabel.text = "${progressBar.progress}%"
            }
        }
    }

    override fun onCompleteClicked(contact: Contact, position: Int) {
        completeContact(contact)
    }

    override fun onDeleteClicked(contact: Contact, position: Int) {

    }

    override fun onContactClicked(contact: Contact, position: Int) {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        activity.startActivity(intent)
    }

    private fun completeContact(contact: Contact) {
        val dialog = activity.showLoading()
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
        var isFromUser = false
        var selectedLeadTemp = option.temps.firstOrNull { it.value == "${option.leadTemp}" }
        leadTempBtn.text = selectedLeadTemp?.text
        val tempsTitle = mutableListOf<String>()
        option.temps.forEach { tempsTitle.add(it.text) }
        leadTempSpinner.adapter = ArrayAdapter<String>(activity,
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

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCompleteTeamReachOutDialog(option: CompleteOption) {
        val dialogView = activity.layoutInflater.inflate(R.layout.alert_complete_team_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
