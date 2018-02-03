package com.hiloipa.app.hilo.utils

import android.app.Activity
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.ActivityCompat
import com.hiloipa.app.hilo.R

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