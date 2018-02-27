/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class BulkUpdate(@JsonProperty("ContactListDropValue") val fieldsAvailable: ArrayList<DropDownValue>,
                 @JsonProperty("Contact_Type") val contactTypes: ArrayList<DropDownValue>,
                 @JsonProperty("Derived_By") val derivedBys: ArrayList<DropDownValue>,
                 @JsonProperty("Group") val groups: ArrayList<DropDownValue>,
                 @JsonProperty("Lead_Temp") val leadTemps: ArrayList<DropDownValue>,
                 @JsonProperty("Pipeline_Position") val pipelinePositions: ArrayList<DropDownValue>,
                 @JsonProperty("Remove_Tag") val tagsToRemove: ArrayList<DropDownValue>)