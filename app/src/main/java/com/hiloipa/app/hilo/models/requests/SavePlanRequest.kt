package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 22.02.2018.
 */
class SavePlanRequest: StandardRequest() {
    @JsonProperty("SelectedPlan") var selectedPlan: Int? = null
    @JsonProperty("custom_reach") lateinit var customReach: String
    @JsonProperty("custom_follow") lateinit var customFollow: String
    @JsonProperty("custom_team") lateinit var customTeam: String
    @JsonProperty("autofollow_hot") lateinit var autofollowHot: String
    @JsonProperty("autofollow_warm") lateinit var autofollowWarm: String
    @JsonProperty("autofollow_cold") lateinit var autofollowCold: String
    @JsonProperty("autofollow_yesno") var autofollowYesNo: Int = 1
}