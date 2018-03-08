/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class Account(@JsonProperty("Company") val company: String?,
              @JsonProperty("Email") val email: String,
              @JsonProperty("First_Name") val firstName: String,
              @JsonProperty("Hilo_BCC_Email") val bccEmail: String,
              @JsonProperty("Last_Name") val lastName: String,
              @JsonProperty("Personal_URL") val personalUrl: String?,
              @JsonProperty("Personal_URL_Business_Site") val businessSite: String?,
              @JsonProperty("Phone_Number") val phoneNumber: String?,
              @JsonProperty("Solution_Tool_Link") val solutionToolLink: String?,
              @JsonProperty("Title") val title: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(company)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(bccEmail)
        parcel.writeString(lastName)
        parcel.writeString(personalUrl)
        parcel.writeString(businessSite)
        parcel.writeString(phoneNumber)
        parcel.writeString(solutionToolLink)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}