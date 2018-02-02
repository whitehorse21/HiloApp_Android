package com.hiloipa.app.hilo.ui.tracker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import kotlinx.android.synthetic.main.fragment_change_plan.*
import kotlinx.android.synthetic.main.layout_plan_custom.*
import kotlinx.android.synthetic.main.layout_plan_keep_lights_on.*
import kotlinx.android.synthetic.main.layout_plan_positioned_for_growth.*
import kotlinx.android.synthetic.main.layout_plan_watch_out_world.*

/**
 * Created by eduardalbu on 02.02.2018.
 */
class ChangePlanFragment: BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var plans: MutableList<RalewayButton>

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
        plans = mutableListOf(keepLightsOnBtn, positionedForGrowthBtn, watchOutWorldBtn, customPlanBtn)
        plans.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View) {
        plans.forEach {
            it.isSelected = it.id == v.id
        }
    }
}