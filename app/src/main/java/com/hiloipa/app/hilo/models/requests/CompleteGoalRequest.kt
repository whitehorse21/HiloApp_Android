/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 01 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class CompleteGoalRequest: StandardRequest() {
    @JsonProperty("TargetID") lateinit var targetId: String
    @JsonProperty("TargetType") lateinit var targetType: String
}