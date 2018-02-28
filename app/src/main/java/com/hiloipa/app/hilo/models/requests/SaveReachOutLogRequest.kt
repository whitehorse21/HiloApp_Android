/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class SaveReachOutLogRequest: StandardRequest() {
    @JsonProperty("ReachoutID") var reachOutId: String = "0"
    @JsonProperty("description") var description: String = ""
    @JsonProperty("date") var date: String = "" // MM-dd-yyyy
    @JsonProperty("time") var time: String = "" // hh:mm aaa
    @JsonProperty("Reachout_Type") var reachType: String = ""
    @JsonProperty("Followup_Date") var followUpDate: String = "" // MM-dd-yyyy
    @JsonProperty("Reachout_other") var reachOutOther: String = ""
}