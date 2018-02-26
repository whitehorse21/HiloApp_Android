package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class CampaignsData(@JsonProperty("campaignsdropdwon") val campaigns: ArrayList<CampaignShort>) {

}

class CampaignShort(@JsonProperty("CampaignID") val id: Int,
                    @JsonProperty("CampaignName") val name: String)