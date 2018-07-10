/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class DisplayContactNotes(@JsonProperty("notes") val notes: ArrayList<ContactNote>)

class ContactNote(@JsonProperty("NotesText") val content: String,
                  @JsonProperty("Notesid") val id: Int,
                  @JsonProperty("Notestitle") val title: String,
                  @JsonProperty("update_time") val updateTime: Date?): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date?)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeSerializable(updateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactNote> {
        override fun createFromParcel(parcel: Parcel): ContactNote {
            return ContactNote(parcel)
        }

        override fun newArray(size: Int): Array<ContactNote?> {
            return arrayOfNulls(size)
        }
    }
}
