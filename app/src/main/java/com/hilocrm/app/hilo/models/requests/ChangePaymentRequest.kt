/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class ChangePaymentRequest: StandardRequest() {
    @JsonProperty("PlanID") var planId: String = "Silver"
    @JsonProperty("PlanDisplayPrice") lateinit var price: String
}