/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class BulkUpdateRequest: StandardRequest() {
    @JsonProperty("ContactID_list") lateinit var contacts: String
    @JsonProperty("Selected_Option") lateinit var selectedOption: String
    @JsonProperty("Selected_Value") lateinit var selectedValue: String
}