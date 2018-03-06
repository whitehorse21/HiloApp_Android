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
class Template(@JsonProperty("Category") val category: String,
               @JsonProperty("Name") val name: String?,
               @JsonProperty("PreviewLink") val previewLink: String,
               @JsonProperty("Shared") val shared: String?,
               @JsonProperty("TemplateID") val templateId: Int,
               @JsonProperty("Type") val type: Int,
               @JsonProperty("UplineName") val uplineName: String?,
               @JsonProperty("UserID") val userId: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(name)
        parcel.writeString(previewLink)
        parcel.writeString(shared)
        parcel.writeInt(templateId)
        parcel.writeInt(type)
        parcel.writeString(uplineName)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Template> {
        override fun createFromParcel(parcel: Parcel): Template {
            return Template(parcel)
        }

        override fun newArray(size: Int): Array<Template?> {
            return arrayOfNulls(size)
        }
    }
}