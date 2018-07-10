package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class GetSmsScriptsRequest: StandardRequest() {
    @JsonProperty("SearchTerm") var searchTerm: String = ""
}