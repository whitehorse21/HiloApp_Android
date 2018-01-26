package com.hiloipa.app.hilo.ui.tracker


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.GoalTrackerAdapter
import com.hiloipa.app.hilo.models.GoalType
import kotlinx.android.synthetic.main.fragment_goal_tracker.*


/**
 * A simple [Fragment] subclass.
 */
class GoalTrackerFragment : Fragment(), TabLayout.OnTabSelectedListener {

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
}
