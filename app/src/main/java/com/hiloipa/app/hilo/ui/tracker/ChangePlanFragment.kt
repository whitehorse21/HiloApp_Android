package com.hiloipa.app.hilo.ui.tracker

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.SavePlanRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.GoalPlan
import com.hiloipa.app.hilo.models.responses.GoalPlans
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_change_plan.*
import kotlinx.android.synthetic.main.layout_plan_auto_follow_up_otions.*
import kotlinx.android.synthetic.main.layout_plan_custom.*
import kotlinx.android.synthetic.main.layout_plan_keep_lights_on.*
import kotlinx.android.synthetic.main.layout_plan_positioned_for_growth.*
import kotlinx.android.synthetic.main.layout_plan_watch_out_world.*

/**
 * Created by eduardalbu on 02.02.2018.
 */
class ChangePlanFragment: BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var plans: MutableList<RalewayButton>
    lateinit var goalPlans: GoalPlans

    companion object {
        fun newInstance(): ChangePlanFragment {
            val args = Bundle()
            val fragment = ChangePlanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_change_plan, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set dismiss buttons click listeners
        changePlanBackBtn.setOnClickListener { this.dismiss() }
        cancelBtn.setOnClickListener { this.dismiss() }
        // add all plans buttons to the list for future use
        keepLightsOnBtn.tag = 1
        positionedForGrowthBtn.tag = 2
        watchOutWorldBtn.tag = 3
        customPlanBtn.tag = 4
        plans = mutableListOf(keepLightsOnBtn, positionedForGrowthBtn, watchOutWorldBtn, customPlanBtn)
        plans.forEach { it.setOnClickListener(this) }

        saveBtn.setOnClickListener { saveGoalPlan() }

        this.showGoalPlans()
    }

    private fun showGoalPlans() {
        val loading = activity.showLoading()
        HiloApp.api().showGoalPlans(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<GoalPlans> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.goalPlans = data
                            updateDefaultValues(goalPlans = goalPlans)
                        } else this.dismiss()
                    } else {
                        activity.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateDefaultValues(goalPlans: GoalPlans = this.goalPlans) {
        // autofollowup
        coldLeadsField.setText("${goalPlans.autoFollow.cold}")
        hotLeadsField.setText("${goalPlans.autoFollow.hot}")
        warmLeadsField.setText("${goalPlans.autoFollow.warm}")
        val checkedId = if (goalPlans.autoFollow.isEnabled) R.id.enableAutoFollowUp else R.id.disableAutoFollowUp
        toggleAutoFollowGroup.check(checkedId)
        // custom plan
        customPlanWeekFollowupsField.setText("${goalPlans.customPlan.follow}")
        customPlanWeekReachoutsField.setText("${goalPlans.customPlan.reach}")
        customPlanWeekTeamNeedsField.setText("${goalPlans.customPlan.team}")
        // set selected plan
        val selectedBtn = plans.firstOrNull { it.tag == goalPlans.currentPlan }
        if (selectedBtn != null)
            onClick(selectedBtn)
    }

    private fun saveGoalPlan() {
        // autofollow details
        val autoFollowEnabled = enableAutoFollowUp.isChecked
        val autoHot = hotLeadsField.text.toString()
        val autoWarm = warmLeadsField.text.toString()
        val autoCold = coldLeadsField.text.toString()
        // custom plan details
        val customReach = customPlanWeekReachoutsField.text.toString()
        val customFollow = customPlanWeekFollowupsField.text.toString()
        val customTeam = customPlanWeekTeamNeedsField.text.toString()

        val request = SavePlanRequest()
        val selectedPlan = plans.firstOrNull { it.isSelected }

        if (selectedPlan == null) {
            Toast.makeText(activity, getString(R.string.select_your_plan), Toast.LENGTH_SHORT).show()
            return
        }

        request.selectedPlan = selectedPlan.tag as Int
        // autofollow
        request.autofollowYesNo = autoFollowEnabled
        request.autofollowHot = autoHot
        request.autofollowWarm = autoWarm
        request.autofollowCold = autoCold
        // custom plan
        request.customReach = customReach
        request.customFollow = customFollow
        request.customTeam = customTeam

        val loading = activity.showLoading()
        HiloApp.api().saveGoalPlan(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()
                        // dismiss plans fragment
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent("update_tracker"))
                        this.dismiss()
                    } else {
                        activity.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onClick(v: View) {
        plans.forEach {
            it.isSelected = it.id == v.id
        }
    }
}