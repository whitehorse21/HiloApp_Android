package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class MessageScript(@JsonProperty("Body") val body: String,
                    @JsonProperty("Category") val category: String,
                    @JsonProperty("ScriptID") val id: Int,
                    @JsonProperty("Title") val title: String,
                    @JsonProperty("UplineName") val uplineName: String?,
                    @JsonProperty("UserID") val userId: Int,
                    @JsonProperty("Type") val type: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(body)
        parcel.writeString(category)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(uplineName)
        parcel.writeInt(userId)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageScript> {
        override fun createFromParcel(parcel: Parcel): MessageScript {
            return MessageScript(parcel)
        }

        override fun newArray(size: Int): Array<MessageScript?> {
            return arrayOfNulls(size)
        }
    }
}