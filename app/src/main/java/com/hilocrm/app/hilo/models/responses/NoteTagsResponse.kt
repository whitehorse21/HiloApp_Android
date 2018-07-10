/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class NoteTagsResponse(@JsonProperty("TagList") val tags: ArrayList<NoteTag>)