package com.hilocrm.app.hilo.models.requests

import android.os.Build
import com.fasterxml.jackson.annotation.JsonProperty
import com.hilocrm.app.hilo.BuildConfig
import com.hilocrm.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 18.02.2018.
 */
class RegistrationRequest(@JsonProperty("FirstName") val firstName: String,
                          @JsonProperty("LastName") val lastName: String,
                          @JsonProperty("Email") val email: String,
                          @JsonProperty("Password") val password: String,
                          @JsonProperty("company_name") val company_Name: String,
                          @JsonProperty("hear") val hear: String,
                          @JsonProperty("hear_othertext") val hear_OtherText: String = "",
                          @JsonProperty("company_id") val company_Id: Int,
                          @JsonProperty("Platform") val platform: String = "Android",
                          @JsonProperty("OS") val os: String = Build.VERSION.RELEASE,
                          @JsonProperty("ScreenSize") val screenSize: String,
                          @JsonProperty("Vendor") val vendor: String = Build.MANUFACTURER,
                          @JsonProperty("AppVersion") val appVersion: String = BuildConfig.VERSION_NAME,
                          @JsonProperty("TimeZone") val timeZone: String = timezone(),
                          @JsonProperty("DeviceToken") val deviceToken: String = "")