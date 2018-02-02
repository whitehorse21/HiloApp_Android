package com.hiloipa.app.hilo.models

import android.support.annotation.StringRes
import com.hiloipa.app.hilo.R

/**
 * Created by eduardalbu on 02.02.2018.
 */
enum class GraphType {
    daily, weekly, monthly;

    companion object {
        fun fromInt(intValue: Int): GraphType = when(intValue) {
            2 -> monthly
            1 -> weekly
            else -> daily
        }
    }

    @StringRes
    fun title(): Int = when(this) {
        daily -> R.string.daily
        weekly -> R.string.weekly
        monthly -> R.string.monthly
    }
}