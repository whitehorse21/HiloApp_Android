package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.hilocrm.app.hilo.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by eduardalbu on 18.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class GoalTrackerResponse(@JsonProperty("Reachouts") val reachOuts: ReachOuts,
                          @JsonProperty("Followups") val followUps: FollowUps,
                          @JsonProperty("TeamReachouts") val teamReachOuts: TeamReachOuts,
                          @JsonProperty("GoalPlan") val goalPlan: GoalPlan,
                          @JsonProperty("GoalDuration") val goalDuration: String,
                          @JsonProperty("HiloMyWeekMsg") val hiloMyWeekMessage: String) : Parcelable {

    @JsonProperty("GoalDurationDropDown")
    lateinit var goalDurations: ArrayList<GoalDurationObjc>

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ReachOuts::class.java.classLoader),
            parcel.readParcelable(FollowUps::class.java.classLoader),
            parcel.readParcelable(TeamReachOuts::class.java.classLoader),
            parcel.readParcelable(GoalPlan::class.java.classLoader),
            parcel.readString(),
            parcel.readString()) {

        parcel.readTypedList(goalDurations, GoalDurationObjc.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(reachOuts, flags)
        parcel.writeParcelable(followUps, flags)
        parcel.writeParcelable(teamReachOuts, flags)
        parcel.writeParcelable(goalPlan, flags)
        parcel.writeString(goalDuration)
        parcel.writeString(hiloMyWeekMessage)
        parcel.writeTypedList(goalDurations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalTrackerResponse> {
        override fun createFromParcel(parcel: Parcel): GoalTrackerResponse {
            return GoalTrackerResponse(parcel)
        }

        override fun newArray(size: Int): Array<GoalTrackerResponse?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ReachOuts(@JsonProperty("Reachouts_Graph") val reachOutsGraphData: GraphData) : Parcelable {

    @JsonProperty("Reachouts")
    lateinit var reachOuts: ArrayList<ReachOutContact>

    constructor(parcel: Parcel) : this(parcel.readParcelable<GraphData>(GraphData::class.java.classLoader)) {
        parcel.readTypedList(reachOuts, ReachOutContact.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(reachOutsGraphData, flags)
        parcel.writeTypedList(reachOuts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReachOuts> {
        override fun createFromParcel(parcel: Parcel): ReachOuts {
            return ReachOuts(parcel)
        }

        override fun newArray(size: Int): Array<ReachOuts?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FollowUps(@JsonProperty("Followups_Graph") val followUpsGraphData: GraphData) : Parcelable {

    @JsonProperty("Followups")
    lateinit var followUpContacts: ArrayList<FollowUpContact>

    constructor(parcel: Parcel) : this(parcel.readParcelable<GraphData>(GraphData::class.java.classLoader)) {
        parcel.readTypedList(followUpContacts, FollowUpContact.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(followUpsGraphData, flags)
        parcel.writeTypedList(followUpContacts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FollowUps> {
        override fun createFromParcel(parcel: Parcel): FollowUps {
            return FollowUps(parcel)
        }

        override fun newArray(size: Int): Array<FollowUps?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TeamReachOuts(@JsonProperty("TeamReachouts_Graph") val teamReachoutsGraphData: GraphData) : Parcelable {

    @JsonProperty("TeamReachouts")
    lateinit var teamReachoutContacts: ArrayList<TeamReachOutContact>

    constructor(parcel: Parcel) : this(parcel.readParcelable<GraphData>(GraphData::class.java.classLoader)) {
        parcel.readTypedList(teamReachoutContacts, TeamReachOutContact.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(teamReachoutsGraphData, flags)
        parcel.writeTypedList(teamReachoutContacts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeamReachOuts> {
        override fun createFromParcel(parcel: Parcel): TeamReachOuts {
            return TeamReachOuts(parcel)
        }

        override fun newArray(size: Int): Array<TeamReachOuts?> {
            return arrayOfNulls(size)
        }
    }
}

// contacts objects
@JsonIgnoreProperties(ignoreUnknown = true)
class ReachOutContact(@JsonProperty("ContactID") id: Int,
                      @JsonProperty("ContactName") name: String,
                      @JsonProperty("priority") priority: String,
                      @JsonProperty("LastModified") lastModified: Date?,
                      @JsonProperty("SlotID") slotId: Int) : Contact(id, name, slotId, lastModified, priority), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(priority)
        parcel.writeSerializable(lastModified)
        parcel.writeInt(slotId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReachOutContact> {
        override fun createFromParcel(parcel: Parcel): ReachOutContact {
            return ReachOutContact(parcel)
        }

        override fun newArray(size: Int): Array<ReachOutContact?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FollowUpContact(@JsonProperty("ContactID") id: Int,
                      @JsonProperty("ContactName") name: String,
                      @JsonProperty("reminderdate") val reminderDate: Date?,
                      @JsonProperty("priority") priority: String,
                      @JsonProperty("days") val days: String?,
                      @JsonProperty("badge") val badge: String,
                      @JsonProperty("LastModified") lastModified: Date?,
                      @JsonProperty("SlotID") slotId: Int) : Contact(id, name, slotId, lastModified, priority), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeSerializable(reminderDate)
        parcel.writeString(priority)
        parcel.writeString(days)
        parcel.writeString(badge)
        parcel.writeSerializable(lastModified)
        parcel.writeInt(slotId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FollowUpContact> {
        override fun createFromParcel(parcel: Parcel): FollowUpContact {
            return FollowUpContact(parcel)
        }

        override fun newArray(size: Int): Array<FollowUpContact?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TeamReachOutContact(@JsonProperty("TeamID") id: Int,
                          @JsonProperty("followupdate") val followUpDate: Date?,
                          @JsonProperty("priority") priority: String?,
                          @JsonProperty("SlotID") slotId: Int,
                          @JsonProperty("badge") val badge: String?,
                          @JsonProperty("days") val days: String?,
                          @JsonProperty("LastModified") lastModified: Date?): Contact(id = id,
        slotId = slotId, lastModified = lastModified), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeSerializable(followUpDate)
        parcel.writeString(priority)
        parcel.writeInt(slotId)
        parcel.writeString(badge)
        parcel.writeString(days)
        parcel.writeSerializable(lastModified)
    }

    @JsonSetter("TeamName")
    fun setTeamName(name: String) {
        this.name = name
    }

    @JsonSetter("ContactName")
    fun setContactName(name: String) {
        this.name = name
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeamReachOutContact> {
        override fun createFromParcel(parcel: Parcel): TeamReachOutContact {
            return TeamReachOutContact(parcel)
        }

        override fun newArray(size: Int): Array<TeamReachOutContact?> {
            return arrayOfNulls(size)
        }
    }
}

open class Contact(val id: Int, var name: String = "",
              val slotId: Int, val lastModified: Date?,
              val priority: String = ""): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(slotId)
        parcel.writeSerializable(lastModified)
        parcel.writeString(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}

// graphs object
@JsonIgnoreProperties(ignoreUnknown = true)
class GraphData(@JsonProperty("Target") val target: Int,
                @JsonProperty("Completed") val completed: Int,
                @JsonProperty("Percentage") val percentage: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(target)
        parcel.writeInt(completed)
        parcel.writeInt(percentage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GraphData> {
        override fun createFromParcel(parcel: Parcel): GraphData {
            return GraphData(parcel)
        }

        override fun newArray(size: Int): Array<GraphData?> {
            return arrayOfNulls(size)
        }
    }
}

// plan object
@JsonIgnoreProperties(ignoreUnknown = true)
class GoalPlan(@JsonProperty("GoalPlanID") val planId: Int,
               @JsonProperty("GoalPlanName") val name: String?,
               @JsonProperty("Reachouts") val reachOuts: Int,
               @JsonProperty("Followups") val followUps: Int,
               @JsonProperty("TeamReachouts") val teamReachOuts: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(planId)
        parcel.writeString(name)
        parcel.writeInt(reachOuts)
        parcel.writeInt(followUps)
        parcel.writeInt(teamReachOuts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalPlan> {
        override fun createFromParcel(parcel: Parcel): GoalPlan {
            return GoalPlan(parcel)
        }

        override fun newArray(size: Int): Array<GoalPlan?> {
            return arrayOfNulls(size)
        }
    }

    @DrawableRes
    fun trackerBGColor(): Int = when (this.planId) {
        1 -> R.drawable.button_plan_keep_lights_on
        2 -> R.drawable.button_plan_positioned_for_growth
        3 -> R.drawable.button_plan_watch_out_world
        else -> R.drawable.button_plan_custom
    }

    @ColorRes
    fun trackerTextColor(): Int = when (this.planId) {
        1 -> R.color.tagBlue
        2 -> R.color.tagGreen600
        3 -> R.color.tagRed
        else -> R.color.tagDeepOrange
    }

    @DrawableRes
    fun planHeader(): Int = when (this.planId) {
        1 -> R.drawable.rectangle_top_rounded_blue
        2 -> R.drawable.rectangle_top_rounded_green
        3 -> R.drawable.rectangle_top_rounded_orange
        else -> R.drawable.rectangle_top_rounded_rose
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class GoalDurationObjc(@JsonProperty("Text") val name: String,
                       @JsonProperty("Value") val value: String,
                       @JsonProperty("Selected") val selected: Boolean) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalDurationObjc> {
        override fun createFromParcel(parcel: Parcel): GoalDurationObjc {
            return GoalDurationObjc(parcel)
        }

        override fun newArray(size: Int): Array<GoalDurationObjc?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is GoalDurationObjc && other.value == value
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + selected.hashCode()
        return result
    }
}
