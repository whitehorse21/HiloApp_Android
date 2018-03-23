/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 23 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class GoalTrackerContacts: StandardRequest() {
    @JsonProperty("ContactName") lateinit var query: String
}