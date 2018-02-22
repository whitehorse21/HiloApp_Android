package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 19.02.2018.
 */
class GoalPlans(@JsonProperty("autofollow") val autoFollow: AutoFollow,
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

    companion object CREATOR : Parcelable.Creator<GoalPlans> {
        override fun createFromParcel(parcel: Parcel): GoalPlans {
            return GoalPlans(parcel)
        }

        override fun newArray(size: Int): Array<GoalPlans?> {
            return arrayOfNulls(size)
        }
    }
}

class AutoFollow(@JsonProperty("autofollow_cold") val cold: Int,
                 @JsonProperty("autofollow_hot") val hot: Int,
                 @JsonProperty("autofollow_warm") val warm: Int,
                 @JsonProperty("autofollow_yesno") val isEnabled: Boolean): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cold)
        parcel.writeInt(hot)
        parcel.writeInt(warm)
        parcel.writeByte(if (isEnabled) 1 else 0)
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