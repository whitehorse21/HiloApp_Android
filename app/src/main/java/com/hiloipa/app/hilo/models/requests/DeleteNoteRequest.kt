/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class DeleteNoteRequest : StandardRequest() {
    @JsonProperty("Notesid")
    lateinit var noteId: String
    @JsonProperty("TagID")
    var tagId: String? = null
}