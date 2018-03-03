package com.hiloipa.app.hilo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager
import java.text.SimpleDateFormat
import java.util.*


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

fun Activity.displaySize(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels
    return Pair(width, height)
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showExplanation(title: String = getString(R.string.error), message: String) {
    val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok), null)
            .create()
    dialog.show()
}

fun Activity.showLoading(message: String? = null): AlertDialog {
    val dialogView = layoutInflater.inflate(R.layout.alert_loading, null)
    val messageLabel: RalewayTextView = dialogView.findViewById(R.id.messageLabel)
    if (message != null) {
        messageLabel.text = message
        messageLabel.visibility = View.VISIBLE
    }
    val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun timezone(): String {
    val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
    val currentLocalTime = calendar.time
    val date = SimpleDateFormat("Z", Locale.getDefault())
    return date.format(currentLocalTime)
}

enum class Status {
    SUCCESS, ERROR;

    companion object {
        fun fromInt(intStatus: Int): Status = if (intStatus == 1) SUCCESS else ERROR
    }
}

fun Int.isSuccess(): Boolean {
    val status = Status.fromInt(this)
    return status == Status.SUCCESS
}