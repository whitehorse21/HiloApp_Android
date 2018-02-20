package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 19.02.2018.
 */
class ShowPlansResponse(@JsonProperty("autofollow") val autoFollow: AutoFollow,
                        @JsonProperty("currentplan") val currentPlan: Int,
                        @JsonProperty("customplan") val customPlan: CustomPlan,
                        @JsonProperty("goaloption") val goalOption: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(AutoFollow::class.java.classLoader),
            parcel.readInt(),
            parcel.readParcelable(CustomPlan::class.java.classLoader),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(autoFollow, flags)
        parcel.writeInt(currentPlan)
        parcel.writeParcelable(customPlan, flags)
        parcel.writeInt(goalOption)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowPlansResponse> {
        override fun createFromParcel(parcel: Parcel): ShowPlansResponse {
            return ShowPlansResponse(parcel)
        }

        override fun newArray(size: Int): Array<ShowPlansResponse?> {
            return arrayOfNulls(size)
        }
    }
}

class AutoFollow(@JsonProperty("autofollow_cold") val cold: Int,
                 @JsonProperty("autofollow_hot") val hot: Int,
                 @JsonProperty("autofollow_warm") val warm: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cold)
        parcel.writeInt(hot)
        parcel.writeInt(warm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutoFollow> {
        override fun createFromParcel(parcel: Parcel): AutoFollow {
            return AutoFollow(parcel)
        }

        override fun newArray(size: Int): Array<AutoFollow?> {
            return arrayOfNulls(size)
        }
    }
}

class CustomPlan(@JsonProperty("custom_follow") val follow: Int,
                 @JsonProperty("custom_reach") val reach: Int,
                 @JsonProperty("custom_team") val team: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(follow)
        parcel.writeInt(reach)
        parcel.writeInt(team)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomPlan> {
        override fun createFromParcel(parcel: Parcel): CustomPlan {
            return CustomPlan(parcel)
        }

        override fun newArray(size: Int): Array<CustomPlan?> {
            return arrayOfNulls(size)
        }
    }
}