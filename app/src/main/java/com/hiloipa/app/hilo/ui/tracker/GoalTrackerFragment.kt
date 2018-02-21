package com.hiloipa.app.hilo.ui.tracker


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
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
import com.hiloipa.app.hilo.models.requests.GoalDuration
import com.hiloipa.app.hilo.models.requests.GoalTrackerRequest
import com.hiloipa.app.hilo.models.responses.GoalDurationObjc
import com.hiloipa.app.hilo.models.responses.GoalTrackerResponse
import com.hiloipa.app.hilo.models.responses.HiloResponse
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
import kotlinx.android.synthetic.main.fragment_goal_tracker.*


/**
 * A simple [Fragment] subclass.
 */
class GoalTrackerFragment : Fragment(), TabLayout.OnTabSelectedListener, GoalTrackerAdapter.ContactClickListener {

    lateinit var adapter: GoalTrackerAdapter
    private lateinit var goalTrackerData: GoalTrackerResponse
    var selectedDuration: GoalDurationObjc? = null

    companion object {
        fun newInstance(): GoalTrackerFragment {
            val args = Bundle()
            val fragment = GoalTrackerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = GoalTrackerAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_goal_tracker, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.delegate = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        tabLayout.setOnTabSelectedListener(this)

        adjustGraphsBtn.setOnClickListener { adjustGraphsSpinner.performClick() }

        showFutureBtn.setOnClickListener {
            val intent = Intent(activity, FutureContactsActivity::class.java)
            val extras = Bundle()
            extras.putInt("goalType", adapter.goalType.toInt())
            intent.putExtras(extras)
            activity.startActivity(intent)
        }

        changePlanBtn.setOnClickListener {
            val dialog = ChangePlanFragment.newInstance()
            dialog.show(childFragmentManager, "ChangePlanFragment")
        }

        hiloMyWeekBtn.setOnClickListener {
            if (selectedDuration != null)
                getTrackerData(GoalDuration.fromString(selectedDuration!!.value), hiloMyWeek = true)
        }

        getTrackerData()
    }

    private fun getTrackerData(duration: GoalDuration = GoalDuration.Weekly, hiloMyWeek: Boolean = false) {
        val dialog = activity.showLoading()
        try {
            val request = GoalTrackerRequest()
            request.goalDuration = duration.name
            request.hiloMyWeek = hiloMyWeek
            HiloApp.api().getGoalTrackerData(goalTrackerRequest = request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<GoalTrackerResponse> ->
                        dialog.dismiss()
                        if (response.status.isSuccess()) {
                            val data = response.data
                            if (data != null) {
                                goalTrackerData = data
                                updateTrackerWithNewData(data)
                            } else {
                                activity.showExplanation(message = getString(R.string.unknown_error))
                            }
                        } else {
                            activity.showExplanation(message = response.message)
                        }
                    }, { error: Throwable ->
                        dialog.dismiss()
                        error.printStackTrace()
                        activity.showExplanation(message = error.localizedMessage)
                    })
        } catch (e: Exception) {
            dialog.dismiss()
            e.printStackTrace()
        }
    }

    private fun updateTrackerWithNewData(data: GoalTrackerResponse = this.goalTrackerData) {
        // setup recycler view and adapter
        recyclerView.adapter = adapter
        adapter.data = data
        adapter.notifyDataSetChanged()
        // setup period spinner
        val periodNames = mutableListOf<String>()
        periodNames.add(getString(R.string.select_period))
        data.goalDurations.forEach { periodNames.add(it.name) }
        adjustGraphsSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, periodNames)
        adjustGraphsSpinner.onItemSelectedListener = onPeriodChangeListener
        // check if there are any selected items in the list or at least with weekly value as default
        val selectedDuration = data.goalDurations.firstOrNull { it.value == data.goalDuration }
        this.selectedDuration = selectedDuration
        // if it finds any set it as selected in the spinner
        if (selectedDuration != null) {
            adjustGraphsBtn.text = getString(R.string.adjust_my_graphs, selectedDuration.name)
            // change graph title text to contain selected duration
            when (adapter.goalType) {
                GoalType.reach_outs -> {
                    reachoutsTypeLabel.text = getString(R.string.reach_outs_s, selectedDuration.name)
                }
                GoalType.follow_ups -> {
                    reachoutsTypeLabel.text = getString(R.string.follow_ups_s, selectedDuration.name)
                }
                GoalType.team_reach_outs -> {
                    reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, selectedDuration.name)
                }
            }

            // update graph details name
            targetTitleLabel.text = getString(R.string.s_ntarget, selectedDuration.value)
            completedTitleLabel.text = getString(R.string.s_ncompleted, selectedDuration.value)
            percentageTitleLabel.text = getString(R.string.s_npercentage, selectedDuration.value)
        }

        // set goal type and update data when the type is changed
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

        // setup current goal plan btn
        currentPlanBtn.text = data.goalPlan.name
        currentPlanBtn.setBackgroundResource(data.goalPlan.trackerBGColor())
        currentPlanBtn.setTextColor(resources.getColor(data.goalPlan.trackerTextColor()))
    }

    private val onPeriodChangeListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (position == 0) {
                if (selectedDuration == null) {
                    adjustGraphsBtn.text = getString(R.string.adjust_my_graphs, getString(R.string.select_period))
                    this@GoalTrackerFragment.selectedDuration = null
                }
            } else {
                // otherwise update ui with new selected item
                this@GoalTrackerFragment.selectedDuration = goalTrackerData.goalDurations[position - 1] // we need to decrement it because we have the first placeholder
                getTrackerData(GoalDuration.fromString(selectedDuration!!.value))
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tabLayout.selectedTabPosition) {
            0 -> {
                adapter.goalType = GoalType.reach_outs
                reachoutsTypeLabel.text = getString(R.string.reach_outs_s, adjustGraphsSpinner.selectedItem as String)
                selectedTypeTitleLabel.text = getString(R.string.new_reach_outs)
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar)
            }
            1 -> {
                adapter.goalType = GoalType.follow_ups
                reachoutsTypeLabel.text = getString(R.string.follow_ups_s, adjustGraphsSpinner.selectedItem as String)
                selectedTypeTitleLabel.text = getString(R.string.follow_ups)
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar_green)
            }
            else -> {
                adapter.goalType = GoalType.team_reach_outs
                reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, adjustGraphsSpinner.selectedItem as String)
                selectedTypeTitleLabel.text = getString(R.string.team_reach_outs)
                progressBar.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar_blue)
            }
        }
        updateTrackerWithNewData()
    }

    override fun onCompleteClicked() {
        when (adapter.goalType) {
            GoalType.follow_ups, GoalType.reach_outs -> this.showCompleteReachOutDialog()
            GoalType.team_reach_outs -> this.showCompleteTeamReachOutDialog()
        }
    }

    override fun onDeleteClicked() {

    }

    override fun onContactClicked() {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        activity.startActivity(intent)
    }

    private fun showCompleteReachOutDialog() {
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
        val scheduleNextFollowUpSpinner: Spinner = dialogView.findViewById(R.id.scheduleNextFollowUpSpinner)
        val contactType: RalewayButton = dialogView.findViewById(R.id.contactTypeBtn)
        val contactTypeSpinner: Spinner = dialogView.findViewById(R.id.contactTypeSpinner)
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

    private fun showCompleteTeamReachOutDialog() {
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
