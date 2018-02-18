package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 18.02.2018.
 */
class GoalTrackerRequest {
    @JsonProperty("UserID") var userId: Int = HiloApp.userData.userId
    @JsonProperty("Api_Access_token") val apiAccessToken: String = HiloApp.instance.getAccessToken()
    @JsonProperty("GoalDuration") var goalDuration: String = GoalDuration.Weekly.name
    @JsonProperty("TimeZone") val timeZone: String = timezone()
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