/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.models.TagColor
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class NotepadNotes(@JsonProperty("notes") val notes: ArrayList<Note>)

@JsonIgnoreProperties(ignoreUnknown = true)
class Note(@JsonProperty("Notesid") val id: Int,
           @JsonProperty("color") val notColor: String,
           @JsonProperty("content") val content: String,
           @JsonProperty("tagName") val tagNames: String,
           @JsonProperty("tagcolor") val tagColors: String,
           @JsonProperty("tags") val tagsIds: String,
           @JsonProperty("time") val time: Date,
           @JsonProperty("title") val title: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(notColor)
        parcel.writeString(content)
        parcel.writeString(tagNames)
        parcel.writeString(tagColors)
        parcel.writeString(tagsIds)
        parcel.writeSerializable(time)
        parcel.writeString(title)
    }

    fun tags(): ArrayList<NoteTag> {
        val tags: ArrayList<NoteTag> = arrayListOf()
        if (tagNames.isNotEmpty() && tagColors.isNotEmpty() && tagsIds.isNotEmpty()) {
            val names = tagNames.split(",")
            val colors = tagColors.split(",")
            val ids = tagsIds.split(",")

            for (i in 0..names.lastIndex) {
                val tag = NoteTag(id = ids[i], name = names[i], color = TagColor.fromString(colors[i]))
                tags.add(tag)
            }
        }

        return tags
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}

class NoteTag(val id: String, val name: String, val color: TagColor)