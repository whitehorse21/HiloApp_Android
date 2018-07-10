package com.hilocrm.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 17.02.2018.
 */
class HiloResponse<T>(@JsonProperty("Data") val data: T?,
                      @JsonProperty("Status") val status: Int,
                      @JsonProperty("Message") val message: String)