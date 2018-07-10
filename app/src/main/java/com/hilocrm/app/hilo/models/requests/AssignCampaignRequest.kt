package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class AssignCampaignRequest: StandardRequest() {
    @JsonProperty("Campaign_id") lateinit var campaignId: String
}