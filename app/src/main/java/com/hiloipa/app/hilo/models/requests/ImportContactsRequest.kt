package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.ui.contacts.DeviceContact

/**
 * Created by eduardalbu on 23.02.2018.
 */
class ImportContactsRequest: StandardRequest() {
    @JsonProperty("Contacts") lateinit var contacts: ArrayList<DeviceContact>
}