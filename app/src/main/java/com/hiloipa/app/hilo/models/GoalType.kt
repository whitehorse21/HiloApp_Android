package com.hiloipa.app.hilo.models

import android.support.annotation.StringRes
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.FollowUpContact
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.ProtoContainer

/**
 * Created by eduardalbu on 26.01.2018.
 */
enum class GoalType {
    reach_outs, follow_ups, team_reach_outs;

    companion object {
        fun fromInt(intValue: Int): GoalType = when (intValue) {
            1 -> follow_ups
            2 -> team_reach_outs
            else -> reach_outs
        }
    }

    fun toInt():Int = when(this) {
        reach_outs -> 0
        follow_ups -> 1
        team_reach_outs -> 2
    }

    @StringRes
    fun description(): Int = when(this) {
        reach_outs -> -1
        follow_ups -> R.string.future_scheduled_follow_ups
        team_reach_outs -> R.string.future_scheduled_team_reach_outs
    }

    @StringRes
    fun title(): Int = when(this) {
        reach_outs -> R.string.reach_outs
        follow_ups -> R.string.follow_ups
        team_reach_outs -> R.string.team_reach_outs
    }

    fun apiValue(): String = when(this) {
        reach_outs -> "Reachout"
        follow_ups -> "Followup"
        team_reach_outs -> "TeamReach"
    }
}