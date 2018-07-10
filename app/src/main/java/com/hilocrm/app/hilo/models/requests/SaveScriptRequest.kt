/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 06 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class SaveScriptRequest: StandardRequest() {
    @JsonProperty("ScriptID") var scriptId: String? = null
    @JsonProperty("Title") lateinit var title: String
    @JsonProperty("Body") lateinit var content: String
}