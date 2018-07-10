package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 18.02.2018.
 */
class GoalTrackerRequest: StandardRequest() {
    @JsonProperty("GoalDuration") var goalDuration: String = GoalDuration.Weekly.name
    @JsonProperty("HiloMyWeek") var hiloMyWeek: Boolean = false
}

enum class GoalDuration {
    Daily, Weekly, Monthly, Yearly;

    companion object {
        fun fromString(stringDuration: String): GoalDuration = when (stringDuration) {
            "Daily" -> Daily
            "Monthly" -> Monthly
            "Yearly" -> Yearly
            else -> Weekly
        }
    }
}