/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Eduard Albu on 08 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class DocumentResponse(@JsonProperty("Documents") val documents: ArrayList<Document>)

class Document(@JsonProperty("docu_title") val title: String,
               @JsonProperty("uploaddate") val uploadDate: Date,
               @JsonProperty("url") val url: String,
               @JsonProperty("doc_id") val docId: String,
               @JsonProperty("type") val type: String,
               @JsonProperty("filename") val fileName: String): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeSerializable(uploadDate)
        parcel.writeString(url)
        parcel.writeString(docId)
        parcel.writeString(type)
        parcel.writeString(fileName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}