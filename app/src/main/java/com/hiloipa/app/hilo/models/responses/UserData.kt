package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 17.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class UserData(@JsonProperty("Api_Access_token") val accessToken: String,
               @JsonProperty("daysremaining") val daysRemaining: String,
               @JsonProperty("expiry") val expiry: Boolean,
               @JsonProperty("expirycount") val expiryCount: Int,
               @JsonProperty("expiryurl") val expiryUrl: String?,
               @JsonProperty("freemonth") val freeMonth: Int?,
               @JsonProperty("messageBody") val messageBody: String?,
               @JsonProperty("messageID") val messageId: String?,
               @JsonProperty("messageImage") val messageImage: String?,
               @JsonProperty("messageTitle") val messageTitle: String?,
               @JsonProperty("totalcontacts") val totalContacts: Int?,
               @JsonProperty("useraccess") val userAccess: String,
               @JsonProperty("usercompany") val userCompany: String,
               @JsonProperty("userid") val userId: Int,
               @JsonProperty("userimage") val userImage: String?,
               @JsonProperty("usermailid") val userMailId: String,
               @JsonProperty("username") val username: String,
               @JsonProperty("userplan") val userPlan: String?,
               @JsonProperty("userstatus") val userStatus: String,
               @JsonProperty("usertype") val userType: String,
               @JsonProperty("visionshow") val visionShow: Boolean): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte().toInt() != 0,
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte().toInt() != 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(daysRemaining)
        parcel.writeInt(if (expiry) 1 else 0)
        parcel.writeInt(expiryCount)
        parcel.writeString(expiryUrl)
        parcel.writeInt(freeMonth ?: -1)
        parcel.writeString(messageBody)
        parcel.writeString(messageId)
        parcel.writeString(messageImage)
        parcel.writeString(messageTitle)
        parcel.writeInt(totalContacts ?: -1)
        parcel.writeString(userAccess)
        parcel.writeString(userCompany)
        parcel.writeInt(userId)
        parcel.writeString(userImage)
        parcel.writeString(userMailId)
        parcel.writeString(username)
        parcel.writeString(userPlan)
        parcel.writeString(userStatus)
        parcel.writeString(userType)
        parcel.writeByte(if (visionShow) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }
}