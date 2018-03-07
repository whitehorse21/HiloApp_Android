/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class UnassignProduct: StandardRequest() {
    @JsonProperty("contact_id") override var contactId: String? = null
    @JsonProperty("ProductID") lateinit var productId: String
    @JsonProperty("ProductType") lateinit var productType: String
}