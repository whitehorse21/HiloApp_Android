package com.hiloipa.app.hilo.ui.base

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

abstract class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        val view = findViewById<View>(android.R.id.content)
        view.isClickable = true
        setupTouchUIException(view, View.OnTouchListener { v, _ ->
            v.postDelayed({ hideSoftKeyboard() }, 100)
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

    private fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(
                AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentFocus != null && currentFocus.windowToken != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    currentFocus.windowToken, 0)
        }
    }
}
