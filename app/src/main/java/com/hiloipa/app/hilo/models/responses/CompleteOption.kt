package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 22.02.2018.
 */
class CompleteOption(@JsonProperty("contactID") val contactId: Int,
                     @JsonProperty("type") val type: String?,
                     @JsonProperty("pipeline") val pipeline: Int,
                     @JsonProperty("contactname") val contactName: String?,
                     @JsonProperty("setfollowup") val setFollowUp: String?,
                     @JsonProperty("leadtemp") val leadTemp: Int,
                     @JsonProperty("reachouttype") val reachOutType: String?,
                     @JsonProperty("reachouttypeother") val reachOutTypeOther: String?,
                     @JsonProperty("reachoutcomments") val reachOutComments: String?,
                     @JsonProperty("contacttype") val contactType: String): Parcelable {

    @JsonProperty("ContactTypeDropValue") lateinit var contactTypes: ArrayList<DropDownValue>
    @JsonProperty("PipelineDropValue") lateinit var pipelines: ArrayList<DropDownValue>
    @JsonProperty("TempDropValue") lateinit var temps: ArrayList<DropDownValue>
    @JsonProperty("reachouttypeList") lateinit var reachOutTypes: ArrayList<DropDownValue>

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {

        parcel.readTypedList(contactTypes, DropDownValue.CREATOR)
        parcel.readTypedList(pipelines, DropDownValue.CREATOR)
        parcel.readTypedList(temps, DropDownValue.CREATOR)
        parcel.readTypedList(reachOutTypes, DropDownValue.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(type)
        parcel.writeInt(pipeline)
        parcel.writeString(contactName)
        parcel.writeString(setFollowUp)
        parcel.writeInt(leadTemp)
        parcel.writeString(reachOutType)
        parcel.writeString(reachOutTypeOther)
        parcel.writeString(reachOutComments)
        parcel.writeString(contactType)
        parcel.writeTypedList(contactTypes)
        parcel.writeTypedList(pipelines)
        parcel.writeTypedList(temps)
        parcel.writeTypedList(reachOutTypes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompleteOption> {
        override fun createFromParcel(parcel: Parcel): CompleteOption {
            return CompleteOption(parcel)
        }

        override fun newArray(size: Int): Array<CompleteOption?> {
            return arrayOfNulls(size)
        }
    }
}

class DropDownValue(@JsonProperty("Text") val text: String,
                    @JsonProperty("Value") val value: String,
                    @JsonProperty("Selected") val selected: Boolean) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(value)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DropDownValue> {
        override fun createFromParcel(parcel: Parcel): DropDownValue {
            return DropDownValue(parcel)
        }

        override fun newArray(size: Int): Array<DropDownValue?> {
            return arrayOfNulls(size)
        }
    }
}