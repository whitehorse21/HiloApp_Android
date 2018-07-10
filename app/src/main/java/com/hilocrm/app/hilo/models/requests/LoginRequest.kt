package com.hilocrm.app.hilo.models.requests

import android.os.Build
import com.fasterxml.jackson.annotation.JsonProperty
import com.hilocrm.app.hilo.BuildConfig
import com.hilocrm.app.hilo.utils.timezone

/**
 * Created by eduardalbu on 17.02.2018.
 */
class LoginRequest(@JsonProperty("Email") val email: String,
                   @JsonProperty("Password") val password: String,
                   @JsonProperty("OS") val os: String = Build.VERSION.RELEASE,
                   @JsonProperty("ScreenSize") val screenSize: String,
                   @JsonProperty("Platform") val platform: String = "Android",
                   @JsonProperty("Vendor") val vendor: String = Build.MANUFACTURER,
                   @JsonProperty("AppVersion") val appVersion: String = BuildConfig.VERSION_NAME,
                   @JsonProperty("TimeZone") val timeZone: String = timezone(),
                   @JsonProperty("DeviceToken") val deviceToken: String = "")