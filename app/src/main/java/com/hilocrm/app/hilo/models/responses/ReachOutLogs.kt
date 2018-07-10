/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class ReachOutLogs(@JsonProperty("CurrentPage") val currentPage: Int,
                   @JsonProperty("ReachoutLogs") val reachOutLogs: ArrayList<ReachOutLog>,
                   @JsonProperty("TotalPages") val totalPages: Int)

class ReachOutLog(@JsonProperty("contact_id") val contactId: Int,
                  @JsonProperty("contactname") val contactName: String,
                  @JsonProperty("descr") val description: String,
                  @JsonProperty("descriptionlog") val descriptionLog: String,
                  @JsonProperty("historyID") val historyID: Int,
                  @JsonProperty("historytype") val historyType: Int,
                  @JsonProperty("sort_time") val sortTime: Date,
                  @JsonProperty("team_id") val teamId: Int,
                  @JsonProperty("typename") val typeName: String,
                  @JsonProperty("update_time") val updateTime: Date?,
                  @JsonProperty("userimage") val userImage: String?): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readSerializable() as Date,
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(contactName)
        parcel.writeString(description)
        parcel.writeString(descriptionLog)
        parcel.writeInt(historyID)
        parcel.writeInt(historyType)
        parcel.writeSerializable(sortTime)
        parcel.writeInt(teamId)
        parcel.writeString(typeName)
        parcel.writeSerializable(updateTime)
        parcel.writeString(userImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReachOutLog> {
        override fun createFromParcel(parcel: Parcel): ReachOutLog {
            return ReachOutLog(parcel)
        }

        override fun newArray(size: Int): Array<ReachOutLog?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is ReachOutLog && other.historyID == historyID
    }

    override fun hashCode(): Int {
        var result = contactId
        result = 31 * result + contactName.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + descriptionLog.hashCode()
        result = 31 * result + historyID
        result = 31 * result + historyType
        result = 31 * result + sortTime.hashCode()
        result = 31 * result + teamId
        result = 31 * result + typeName.hashCode()
        result = 31 * result + (updateTime?.hashCode() ?: 0)
        result = 31 * result + (userImage?.hashCode() ?: 0)
        return result
    }
}