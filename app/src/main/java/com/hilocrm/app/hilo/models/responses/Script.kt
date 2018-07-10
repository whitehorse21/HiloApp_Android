/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class Script(@JsonProperty("Body") val body: String,
             @JsonProperty("Category") val category: String,
             @JsonProperty("ScriptID") val id: Int,
             @JsonProperty("Title") val title: String,
             @JsonProperty("Type") val type: String,
             @JsonProperty("UplineName") val uplineName: String?,
             @JsonProperty("UserID") val userId: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(body)
        parcel.writeString(category)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(uplineName)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Script> {
        override fun createFromParcel(parcel: Parcel): Script {
            return Script(parcel)
        }

        override fun newArray(size: Int): Array<Script?> {
            return arrayOfNulls(size)
        }
    }
}