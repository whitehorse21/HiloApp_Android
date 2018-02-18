package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by eduardalbu on 18.02.2018.
 */
class GoalTrackerResponse(@JsonProperty("Reachouts") val reachOuts: ReachOuts,
                          @JsonProperty("Followups") val followUps: FollowUps,
                          @JsonProperty("TeamReachouts") val teamReachOuts: TeamReachOuts,
                          @JsonProperty("GoalPlan") val goalPlan: GoalPlan,
                          @JsonProperty("GoalDuration") val goalDuration: String,
                          @JsonProperty("HiloMyWeekMsg") val hiloMyWeekMessage: String): Parcelable {

    @JsonProperty("GoalDurationDropDown") lateinit var goalDurations: ArrayList<GoalDurationObjc>

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

class ReachOuts(@JsonProperty("Reachouts_Graph") val reachOutsGraphData: GraphData): Parcelable {

    @JsonProperty("Reachouts") lateinit var reachOuts: ArrayList<ReachOutContact>

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

class FollowUps(@JsonProperty("Followups_Graph") val followUpsGraphData: GraphData): Parcelable {

    @JsonProperty("Followups") lateinit var followUpContacts: ArrayList<FollowUpContact>

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

class TeamReachOuts(@JsonProperty("TeamReachouts_Graph") val teamReachoutsGraphData: GraphData) : Parcelable {

    @JsonProperty("TeamReachouts") lateinit var teamReachoutContacts: ArrayList<TeamReachOutContact>

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
class ReachOutContact(@JsonProperty("ContactID") val contactId: Int,
                      @JsonProperty("ContactName") val contactName: String,
                      @JsonProperty("priority") val priority: String,
                      @JsonProperty("LastModified") val lastModified: Date?,
                      @JsonProperty("SlotID") val slotId: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(contactName)
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

class FollowUpContact(@JsonProperty("ContactID") val contactId: Int,
                      @JsonProperty("ContactName") val contactName: String,
                      @JsonProperty("reminderdate") val reminderDate: Date?,
                      @JsonProperty("priority") val priority: String,
                      @JsonProperty("badge") val badge: String,
                      @JsonProperty("LastModified") val lastModified: Date?,
                      @JsonProperty("SlotID") val slotId: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(contactName)
        parcel.writeSerializable(reminderDate)
        parcel.writeString(priority)
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

class TeamReachOutContact(@JsonProperty("TeamID") val teamId: Int,
                          @JsonProperty("TeamName") val teamName: String,
                          @JsonProperty("followupdate") val followUpDate: Date?,
                          @JsonProperty("SlotID") val slotId: Int,
                          @JsonProperty("LastModified") val lastModified: Date?): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readInt(),
            parcel.readSerializable() as Date)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(teamId)
        parcel.writeString(teamName)
        parcel.writeSerializable(followUpDate)
        parcel.writeInt(slotId)
        parcel.writeSerializable(lastModified)
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

// graphs object
class GraphData(@JsonProperty("Target") val target: Int,
                @JsonProperty("Completed") val completed: Int,
                @JsonProperty("Percentage") val percentage: Int): Parcelable {

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
class GoalPlan(@JsonProperty("GoalPlanID") val planId: Int,
               @JsonProperty("GoalPlanName") val name: String,
               @JsonProperty("Reachouts") val reachOuts: Int,
               @JsonProperty("Followups") val followUps: Int,
               @JsonProperty("TeamReachouts") val teamReachOuts: Int): Parcelable {

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
}

class GoalDurationObjc(@JsonProperty("Text") val name: String,
                       @JsonProperty("Value") val value: String,
                       @JsonProperty("Selected") val selected: Boolean): Parcelable {

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
}
