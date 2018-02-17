package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.utils.HiloApp

/**
 * Created by eduardalbu on 17.02.2018.
 */
class LogoutRequest(@JsonProperty("Api_Access_token") val accessToken: String = HiloApp.instance.getAccessToken(),
                    @JsonProperty("UserID") val userId: String)