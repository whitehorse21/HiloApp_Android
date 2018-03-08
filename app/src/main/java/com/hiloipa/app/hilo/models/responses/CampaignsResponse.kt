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
class CampaignsResponse(@JsonProperty("campaigns") val campaigs: ArrayList<Campaign>,
                        @JsonProperty("campaignsdropdown") val values: ArrayList<CampaignValue>?)

@JsonIgnoreProperties(ignoreUnknown = true)
class CampaignValue(@JsonProperty("CampaignID") val id: Int,
                    @JsonProperty("CampaignName") val name: String)

@JsonIgnoreProperties(ignoreUnknown = true)
class Campaign(@JsonProperty("Campaign") val campaign: String,
               @JsonProperty("Message_title") val messageTitle: String,
               @JsonProperty("Processtatus") val status: String,
               @JsonProperty("assign_id") val assignId: Int,
               @JsonProperty("senddate") val sendDate: Date): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(campaign)
        parcel.writeString(messageTitle)
        parcel.writeString(status)
        parcel.writeInt(assignId)
        parcel.writeSerializable(sendDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Campaign> {
        override fun createFromParcel(parcel: Parcel): Campaign {
            return Campaign(parcel)
        }

        override fun newArray(size: Int): Array<Campaign?> {
            return arrayOfNulls(size)
        }
    }
}