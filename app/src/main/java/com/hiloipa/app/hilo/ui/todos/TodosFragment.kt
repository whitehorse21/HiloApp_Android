package com.hiloipa.app.hilo.ui.todos


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.ToDoData
import com.hiloipa.app.hilo.models.responses.TodoType
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_todos.*


/**
 * A simple [Fragment] subclass.
 */
class TodosFragment : Fragment() {

    lateinit var toDoData: ToDoData

    companion object {
        fun newInstance(): TodosFragment {
            val args = Bundle()
            val fragment = TodosFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_todos, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver,
                IntentFilter(TodoDetailsFragment.actionUpdate))

        goalsButton.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.goals),
                TodoType.goal, toDoData.goals).show(childFragmentManager, "GoalsFragment") }

        actionsButton.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.actions),
                TodoType.action, toDoData.actions).show(childFragmentManager, "ActionsFragment") }

        teamNeedsBtn.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.team_needs),
                TodoType.need, toDoData.teamNeeds).show(childFragmentManager, "TeamNeedsFragment") }

        calendarEvetsBtn.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.calendar_events),
                TodoType.event, toDoData.events).show(childFragmentManager, "CalendarEventsFragment") }

        getDashboardData()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when(intent.action) {
                TodoDetailsFragment.actionUpdate -> getDashboardData()
            }
        }
    }

    private fun getDashboardData() {
        val loading = activity.showLoading()
        HiloApp.api().getToDoDashboard(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ToDoData> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            toDoData = data
                            updateUIWithNewData()
                        }
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateUIWithNewData() {
        val goalsCount = toDoData.goals.size
        val actionsCount = toDoData.actions.size
        val needsCount = toDoData.teamNeeds.size
        val eventsCount = toDoData.events.size

        goalsLabel.text = getString(R.string.d_goals_pending, goalsCount)
        goalsCountLabel.text = "$goalsCount"

        actionsLabel.text = getString(R.string.d_actions_pending, actionsCount)
        actionsCountLabel.text = "$actionsCount"

        teamNeedsLabel.text = getString(R.string.d_team_needs_pending, needsCount)
        teamNeedsCountLabel.text = "$needsCount"

        eventsLabel.text = getString(R.string.d_events_scheduled, eventsCount)
        eventsCountLabel.text = "$eventsCount"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver)
    }
}
