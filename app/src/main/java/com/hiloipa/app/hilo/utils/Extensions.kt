package com.hiloipa.app.hilo.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.view.inputmethod.InputMethodManager
import com.hiloipa.app.hilo.R
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

/**
 * Created by eduardalbu on 03.02.2018.
 */
fun Activity.openUrl(url: String) {
    val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(resources.getColor(R.color.colorPrimary))
            .build()
    intent.launchUrl(this, Uri.parse(url))
}

fun Activity.checkForCrashes() {
    CrashManager.register(this)
}

fun Activity.checkForUpdates() {
    // Remove this for store builds!
    UpdateManager.register(this)
}

fun Activity.unregisterManagers() {
    UpdateManager.unregister()
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}