package com.hiloipa.app.hilo.ui.tracker

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R

/**
 * Created by eduardalbu on 02.02.2018.
 */
class ChangePlanFragment: BottomSheetDialogFragment() {

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
    }
}