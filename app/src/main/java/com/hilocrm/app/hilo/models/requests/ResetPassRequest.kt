package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 18.02.2018.
 */
class ResetPassRequest(@JsonProperty("email") val email: String)