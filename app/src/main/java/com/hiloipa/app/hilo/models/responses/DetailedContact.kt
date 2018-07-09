package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by eduardalbu on 22.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class DetailedContact(@JsonProperty("contact_id") id: Int,
                      @JsonProperty("contact_number") var contactNumber: String?,
                      @JsonProperty("contact_type") val contactType: String?,
                      @JsonProperty("contact_type_position") val contactTypePos: Int,
                      @JsonProperty("email") val email: String,
                      @JsonProperty("first_name") val firstName: String,
                      @JsonProperty("group_position") val groupPosition: Int,
                      @JsonProperty("groups") val groups: String?,
                      @JsonProperty("last_name") val lastName: String,
                      @JsonProperty("last_reachedout") val lastReachOut: Date?,
                      @JsonProperty("pipeline") val pipeline: Int,
                      @JsonProperty("pipeline_position") val pipelinePos: String?,
                      @JsonProperty("temp_id") val tempId: Int,
                      @JsonProperty("temp_name") val tempName: String,
                      @JsonProperty("user_image") val userImage: String): Parcelable, Contact(id, "$firstName $lastName", 0, Date()) {

    @JsonIgnore
    var isSelected = false

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
        isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(contactNumber)
        parcel.writeString(contactType)
        parcel.writeInt(contactTypePos)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeInt(groupPosition)
        parcel.writeString(groups)
        parcel.writeString(lastName)
        parcel.writeSerializable(lastReachOut)
        parcel.writeInt(pipeline)
        parcel.writeString(pipelinePos)
        parcel.writeInt(tempId)
        parcel.writeString(tempName)
        parcel.writeString(userImage)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailedContact> {
        override fun createFromParcel(parcel: Parcel): DetailedContact {
            return DetailedContact(parcel)
        }

        override fun newArray(size: Int): Array<DetailedContact?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is DetailedContact && other.id == id
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (contactNumber?.hashCode() ?: 0)
        result = 31 * result + (contactType?.hashCode() ?: 0)
        result = 31 * result + contactTypePos
        result = 31 * result + email.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + groupPosition
        result = 31 * result + (groups?.hashCode() ?: 0)
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (lastReachOut?.hashCode() ?: 0)
        result = 31 * result + pipeline
        result = 31 * result + pipelinePos!!.hashCode()
        result = 31 * result + tempId
        result = 31 * result + tempName.hashCode()
        result = 31 * result + userImage.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}

class DetailedContacs(@JsonProperty("CurrentPage") val currentPage: Int,
                      @JsonProperty("ForceUpdate") val forceUpdate: String,
                      @JsonProperty("TotalPages") val totalPages: Int): Parcelable {

    @JsonProperty("ContactsList") lateinit var contacts: ArrayList<DetailedContact>

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt()) {
        parcel.readTypedList(contacts, DetailedContact.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(currentPage)
        parcel.writeString(forceUpdate)
        parcel.writeInt(totalPages)
        parcel.writeTypedList(contacts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailedContacs> {
        override fun createFromParcel(parcel: Parcel): DetailedContacs {
            return DetailedContacs(parcel)
        }

        override fun newArray(size: Int): Array<DetailedContacs?> {
            return arrayOfNulls(size)
        }
    }
}