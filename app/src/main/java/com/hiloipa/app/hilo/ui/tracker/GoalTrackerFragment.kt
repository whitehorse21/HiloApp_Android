package com.hiloipa.app.hilo.ui.tracker


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
import android.widget.Button
import android.widget.Spinner

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.GoalTrackerAdapter
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import kotlinx.android.synthetic.main.fragment_goal_tracker.*


/**
 * A simple [Fragment] subclass.
 */
class GoalTrackerFragment : Fragment(), TabLayout.OnTabSelectedListener, GoalTrackerAdapter.ContactClickListener {

    lateinit var adapter: GoalTrackerAdapter

    companion object {
        fun newInstance(): GoalTrackerFragment {
            val args = Bundle()
            val fragment = GoalTrackerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_goal_tracker, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GoalTrackerAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        tabLayout.setOnTabSelectedListener(this)
        // setup graphs spinner
        adjustGraphsSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, arrayOf("Daily % Complete",
                "Weekly % Complete", "Monthly % Complete"))

        adjustGraphsBtn.setOnClickListener { adjustGraphsSpinner.performClick() }

        adjustGraphsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position) as String
                adjustGraphsBtn.text = getString(R.string.adjust_my_graphs, item)
                when(adapter.goalType) {
                    GoalType.reach_outs -> reachoutsTypeLabel.text = getString(R.string.reach_outs_s, item)
                    GoalType.follow_ups -> reachoutsTypeLabel.text = getString(R.string.follow_ups_s, item)
                    GoalType.team_reach_outs -> reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, item)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tabLayout.selectedTabPosition) {
            0 -> {
                adapter.goalType = GoalType.reach_outs
                reachoutsTypeLabel.text = getString(R.string.reach_outs_s, adjustGraphsSpinner.selectedItem as String)
            }
            1 -> {
                adapter.goalType = GoalType.follow_ups
                reachoutsTypeLabel.text = getString(R.string.follow_ups_s, adjustGraphsSpinner.selectedItem as String)
            }
            else -> {
                adapter.goalType = GoalType.team_reach_outs
                reachoutsTypeLabel.text = getString(R.string.team_reach_outs_s, adjustGraphsSpinner.selectedItem as String)
            }
        }
    }

    override fun onCompleteClicked() {
        this.showCompleteReachOutDialog()
    }

    override fun onDeleteClicked() {

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
        val dialogView = activity.layoutInflater.inflate(R.layout.alert_complete_reach_out, null)
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
