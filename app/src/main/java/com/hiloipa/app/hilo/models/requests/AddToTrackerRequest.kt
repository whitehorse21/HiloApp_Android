package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class AddToTrackerRequest: StandardRequest() {
    @JsonProperty("ContactsList") lateinit var list: String
}