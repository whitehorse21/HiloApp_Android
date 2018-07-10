/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class SaveContactNote: StandardRequest() {
    @JsonProperty("NotesValue") lateinit var content: String
    @JsonProperty("Notesid") var noteId: String = "0"
    @JsonProperty("Notestitle") lateinit var title: String
}