/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class SaveNoteRequest:StandardRequest() {
    @JsonProperty("NotesValue") lateinit var noteText: String
    @JsonProperty("Notesid") var noteId: String = "0"
    @JsonProperty("Notescolor") lateinit var noteColor: String
    @JsonProperty("Notestitle") lateinit var noteTitle: String
    @JsonProperty("tags") var tags: String = ""
    @JsonProperty("contactid") override var contactId: String? = ""
}