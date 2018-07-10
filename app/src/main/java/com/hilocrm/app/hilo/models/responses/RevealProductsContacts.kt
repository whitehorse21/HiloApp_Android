/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class RevealProductsContacts(@JsonProperty("contacts") val contacts: ArrayList<DashContact>)
