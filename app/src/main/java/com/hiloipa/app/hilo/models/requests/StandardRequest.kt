package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 19.02.2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
open class StandardRequest {
    @JsonProperty("UserID") var userId: String = "${HiloApp.userData.userId}"
    @JsonProperty("Api_Access_token") val accessToken: String = HiloApp.instance.getAccessToken()
    @JsonProperty("TimeZone") val timeZone: String = timezone()
    @JsonProperty("Type") var type: String? = null
    @JsonProperty("ContactID") open var contactId: String? = null
    @JsonProperty("ContactName") var contactName: String? = null
}