/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 03 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ActionForGoalRequest: StandardRequest() {
    @JsonProperty("task_title") lateinit var title: String
    @JsonProperty("due_date") var dueDate: String? = null
    @JsonProperty("Details") var details: String? = null
    @JsonProperty("Alldays") var allDay: String = "false"
    @JsonProperty("priority") var priority: String? = null
    @JsonProperty("time") var time: String? = null
    @JsonProperty("session") var session: String? = null
    @JsonProperty("contactid") override var contactId: String? = "0"
    @JsonProperty("taskid") var taskId: String? = null
    @JsonProperty("goalactionid") var goalActions: String? = null
}