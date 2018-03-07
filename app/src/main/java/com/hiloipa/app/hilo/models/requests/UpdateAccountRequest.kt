/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class UpdateAccountRequest: StandardRequest() {
    @JsonProperty("First_Name") lateinit var firstName: String
    @JsonProperty("Last_Name") lateinit var lastName: String
    @JsonProperty("Email") lateinit var email: String
    @JsonProperty("Hilo_BCC_Email") lateinit var bccEmail: String
    @JsonProperty("Phone_Number") lateinit var phoneNumber: String
    @JsonProperty("Personal_URL") lateinit var personalUrl: String
    @JsonProperty("Personal_URL_Business_Site") lateinit var businessSite: String
    @JsonProperty("Solution_Tool_Link") lateinit var solutionToolLink: String
    @JsonProperty("Title") lateinit var title: String
    @JsonProperty("Company") lateinit var company: String
}