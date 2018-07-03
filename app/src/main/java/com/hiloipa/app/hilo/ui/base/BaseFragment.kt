package com.hiloipa.app.hilo.ui.base

import android.app.Activity
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

abstract class BaseFragment : Fragment() {

    private lateinit var rootView: View

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view.rootView
        rootView.isClickable = true
    }

    override fun onResume() {
        super.onResume()
        setupTouchUIException(rootView, View.OnTouchListener { v, _ ->
            v.postDelayed({ hideSoftKeyboard(activity) }, 100)
            false
        }, null)
    }

    private fun setupTouchUIException(view: View?, onTouchListener: View.OnTouchListener,
                                      vararg exceptIDs: View?) {
        if (view != null) {
            val ids: MutableList<View>
            ids = ArrayList(exceptIDs.size)
            for (v in exceptIDs) {
                if (v != null) ids.add(v)
            }

            //Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                if (!ids.isEmpty() && ids.contains(view)) {
                    return
                }
                view.setOnTouchListener(onTouchListener)
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupTouchUIException(innerView, onTouchListener, *exceptIDs)
                }
            }
        }
    }
    private fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null) {
            val inputMethodManager = activity.getSystemService(
                    AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

            if (activity.currentFocus != null && activity.currentFocus.windowToken != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus.windowToken, 0)

            }
        }
    }
}
