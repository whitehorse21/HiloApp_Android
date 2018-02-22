package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 23.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ImportContactsResponse(@JsonProperty("Imported") val imported: Int,
                             @JsonProperty("Skipped") val skipped: Int)