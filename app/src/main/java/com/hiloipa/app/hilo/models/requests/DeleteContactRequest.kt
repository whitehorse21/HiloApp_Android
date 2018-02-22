package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 23.02.2018.
 */
class DeleteContactRequest: StandardRequest() {
    @JsonProperty("contactid") lateinit var id: String
}