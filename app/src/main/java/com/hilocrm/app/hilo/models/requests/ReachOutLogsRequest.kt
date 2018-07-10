/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ReachOutLogsRequest: StandardRequest() {
    @JsonProperty("page") var page: Int = 1
    @JsonProperty("Search") var search: String? = null
}