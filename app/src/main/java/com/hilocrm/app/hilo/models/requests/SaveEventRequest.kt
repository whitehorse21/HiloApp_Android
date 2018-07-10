/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 04 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class SaveEventRequest: StandardRequest() {
    @JsonProperty("EventName") lateinit var name: String
    @JsonProperty("EventsType") lateinit var eventType: String
    @JsonProperty("Date") var date: String? = null
    @JsonProperty("Time") var time: String? = null
    @JsonProperty("End_Date") var endDate: String? = null
    @JsonProperty("EndTime") var endTime: String? = null
    @JsonProperty("Location") var location: String? = null
    @JsonProperty("Alldays") var allDay: String = "false"
    @JsonProperty("Notes") var details: String? = null
    @JsonProperty("AMPM") var startAmPm: String? = null
    @JsonProperty("EndAMPM") var endAmPm: String? = null
    @JsonProperty("ContactID") override var contactId: String? = null
    @JsonProperty("EventID") var eventId: String = "0"
}