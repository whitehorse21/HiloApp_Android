/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class AssignProductRequest: StandardRequest() {
    @JsonProperty("contact_id") override var contactId: String? = null
    @JsonProperty("Product_id") lateinit var productId: String
    @JsonProperty("Product_Type") lateinit var productType: String
    @JsonProperty("Ship DateTime") val shipDateTime: String = ""
}