package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 22.02.2018.
 */
class ContactsListRequest: StandardRequest() {
    @JsonProperty("page") var page: Int = 1
    @JsonProperty("Search") lateinit var query: String
}