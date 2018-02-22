package com.hiloipa.app.hilo.ui.tracker


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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.PagerAdapter
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.requests.GoalDuration
import com.hiloipa.app.hilo.models.requests.GoalTrackerRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.GoalDurationObjc
import com.hiloipa.app.hilo.models.responses.GoalTrackerResponse
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.SearchContact
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_goal_tracker.*


/**
 * A simple [Fragment] subclass.
 */
class GoalTrackerFragment : Fragment() {

    lateinit var adapter: PagerAdapter
    private lateinit var goalTrackerData: GoalTrackerResponse
    var selectedDuration: GoalDurationObjc? = null
    var fragments = mutableMapOf<Int, GoalFragment>()

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
        val filter = IntentFilter("update_tracker")
        activity.registerReceiver(broadcastReceiver, filter)
        adapter = PagerAdapter(activity, childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit = 4
        // set buttons listeners
        adjustGraphsBtn.setOnClickListener { adjustGraphsSpinner.performClick() }

        changePlanBtn.setOnClickListener {
            val dialog = ChangePlanFragment.newInstance()
            dialog.show(childFragmentManager, "ChangePlanFragment")
        }

        hiloMyWeekBtn.setOnClickListener {
            if (selectedDuration != null)
                getTrackerData(GoalDuration.fromString(selectedDuration!!.value), hiloMyWeek = true)
        }

        // get data from server
        getTrackerData()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when (intent.action) {
                "update_tracker" -> getTrackerData()
            }
        }
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
        val dialog = activity.showLoading()
        // setup recycler view and adapter
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
        if (selectedDuration != null)
            adjustGraphsBtn.text = getString(R.string.adjust_my_graphs, selectedDuration.name)

        // setup current goal plan btn
        currentPlanBtn.text = data.goalPlan.name
        currentPlanBtn.setBackgroundResource(data.goalPlan.trackerBGColor())
        currentPlanBtn.setTextColor(resources.getColor(data.goalPlan.trackerTextColor()))
        if (viewPager.adapter == null) {
            setupPagerAdapter(data).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        viewPager.adapter = it
                        dialog.dismiss()
                    }.subscribe()
        } else {
            fragments.forEach { keySet -> keySet.value.updateFragmentData(data, selectedDuration!!) }
            dialog.dismiss()
        }

        getAllContactsFromServer()
    }

    private fun setupPagerAdapter(data: GoalTrackerResponse): Observable<PagerAdapter> = Observable.create {
        val adapter = PagerAdapter(activity, childFragmentManager)
        if (this.selectedDuration != null) {
            if (adapter.count == 0) {
                fragments.put(GoalType.reach_outs.toInt(),
                        GoalFragment.newInstance(data, GoalType.reach_outs, selectedDuration!!))

                fragments.put(GoalType.follow_ups.toInt(),
                        GoalFragment.newInstance(data, GoalType.follow_ups, selectedDuration!!))

                fragments.put(GoalType.team_reach_outs.toInt(),
                        GoalFragment.newInstance(data, GoalType.team_reach_outs, selectedDuration!!))

                fragments.forEach { adapter.addFragment(it.value, getString(GoalType.fromInt(it.key).title())) }
            }
        }

        it.onNext(adapter)
        it.onComplete()
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

    private fun getAllContactsFromServer() {
        val request = StandardRequest()
        HiloApp.api().searchContacts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ArrayList<SearchContact>> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            val intent = Intent("contacts_ready")
                            val extras = Bundle()
                            extras.putParcelableArrayList("contacts", data)
                            intent.putExtras(extras)
                            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent)
                        }
                    }
                }, { error: Throwable ->
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.unregisterReceiver(broadcastReceiver)
    }
}
