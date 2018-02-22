package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 22.02.2018.
 */
class CompleteReachRequest : StandardRequest() {
    @JsonProperty("Type") var goalType: String = ""
    @JsonProperty("pipeline") var pipeline: String = ""
    @JsonProperty("setfollowup") var nextFollowUp: String = ""
    @JsonProperty("leadtemp") var leadTemp: String = ""
    @JsonProperty("reachouttype") var reachOutType: String = ""
    @JsonProperty("reachoutcomments") var reachOutComments: String = ""
    @JsonProperty("contacttype") var contactType: String = ""
}