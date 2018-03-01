/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.models.responses

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
class ToDoData(): Parcelable {

    @JsonProperty("Followup") lateinit var followUps: ArrayList<Followup>
    @JsonProperty("Reacheout") lateinit var reachOuts: ArrayList<Reachout>
    @JsonProperty("Events") lateinit var events: ArrayList<Event>
    @JsonProperty("Actions") lateinit var actions: ArrayList<Action>
    @JsonProperty("Goals") lateinit var goals: ArrayList<Goal>
    @JsonProperty("TeamNeeds") lateinit var teamNeeds: ArrayList<TeamNeed>
    @JsonProperty("contacts") lateinit var contacts: ArrayList<DashContact>
    @JsonProperty("Timestamp") lateinit var timestamp: Date

    constructor(parcel: Parcel): this() {
        parcel.readTypedList(followUps, Followup.CREATOR)
        parcel.readTypedList(reachOuts, Reachout.CREATOR)
        parcel.readTypedList(events, Event.CREATOR)
        parcel.readTypedList(actions, Action.CREATOR)
        parcel.readTypedList(goals, Goal.CREATOR)
        parcel.readTypedList(teamNeeds, TeamNeed.CREATOR)
        parcel.readTypedList(contacts, DashContact.CREATOR)
        timestamp = parcel.readSerializable() as Date
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(followUps)
        parcel.writeTypedList(reachOuts)
        parcel.writeTypedList(events)
        parcel.writeTypedList(actions)
        parcel.writeTypedList(goals)
        parcel.writeTypedList(teamNeeds)
        parcel.writeTypedList(contacts)
        parcel.writeSerializable(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDoData> {
        override fun createFromParcel(parcel: Parcel): ToDoData {
            return ToDoData(parcel)
        }

        override fun newArray(size: Int): Array<ToDoData?> {
            return arrayOfNulls(size)
        }
    }
}

class Event(@JsonProperty("event_id") id: Int,
            @JsonProperty("event_title") name: String,
            @JsonProperty("StartDate") val startDate: Date,
            @JsonProperty("StartTime") val startTime: String,
            @JsonProperty("StartSession") val startSession: String,
            @JsonProperty("EndTime") val endTime: String,
            @JsonProperty("EndSession") val endSession: String,
            @JsonProperty("ContactID") contactId: String,
            @JsonProperty("ContactName") contactName: String?,
            @JsonProperty("Details") details: String?,
            @JsonProperty("location") val location: String?,
            @JsonProperty("event_type") val eventType: String,
            @JsonProperty("status") status: Int,
            @JsonProperty("created_datetime") created: Date,
            @JsonProperty("updated_datetime") updated: Date?,
            @JsonProperty("EndDate") val endDate: Date,
            @JsonProperty("allday") allDay: Boolean): ToDo(id, 0, null, contactId,
        contactName, 0, name, created, updated, null, null, null, status, allDay, details),
        Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date?,
            parcel.readSerializable() as Date,
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeSerializable(startDate)
        parcel.writeString(startTime)
        parcel.writeString(startSession)
        parcel.writeString(endTime)
        parcel.writeString(endSession)
        parcel.writeString(contactId)
        parcel.writeString(contactName)
        parcel.writeString(details)
        parcel.writeString(location)
        parcel.writeString(eventType)
        parcel.writeInt(status)
        parcel.writeSerializable(created)
        parcel.writeSerializable(updated)
        parcel.writeSerializable(endDate)
        parcel.writeByte(if (allDay == true) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}

class Action(@JsonProperty("TaskID") id: Int,
             @JsonProperty("priority_id") priorityId: Int,
             @JsonProperty("priority") priority: String?,
             @JsonProperty("TaskName") name: String,
             @JsonProperty("time") time: String?,
             @JsonProperty("session") session: String?,
             @JsonProperty("createddate") created: Date,
             @JsonProperty("duedate") dueDate: Date?,
             @JsonProperty("user_id") userId: Int,
             @JsonProperty("ContactID") contactId: String,
             @JsonProperty("ContactName") contactName: String?,
             @JsonProperty("Details") details: String?,
             @JsonProperty("status") status: Int,
             @JsonProperty("updated_datetime") updated: Date?,
             @JsonProperty("allday") allDay: Boolean) : ToDo(id, priorityId, priority, contactId,
        contactName, userId, name, created, updated, dueDate, time, session, status, allDay, details),
        Parcelable {

    @JsonProperty("goal_id") var goalId: List<ShortValue> = listOf()

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date?,
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date?,
            parcel.readByte() != 0.toByte()) {
        parcel.readTypedList(goalId, ShortValue.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(name)
        parcel.writeString(time)
        parcel.writeString(session)
        parcel.writeSerializable(created)
        parcel.writeSerializable(dueDate)
        parcel.writeInt(userId)
        parcel.writeString(contactId)
        parcel.writeString(contactName)
        parcel.writeString(details)
        parcel.writeInt(status)
        parcel.writeSerializable(updated)
        parcel.writeByte(if (allDay == true) 1 else 0)
        parcel.writeTypedList(goalId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Action> {
        override fun createFromParcel(parcel: Parcel): Action {
            return Action(parcel)
        }

        override fun newArray(size: Int): Array<Action?> {
            return arrayOfNulls(size)
        }
    }
}

class Goal(@JsonProperty("GoalsID") id: Int,
           @JsonProperty("priority_id") priorityId: Int,
           @JsonProperty("priority") priority: String?,
           @JsonProperty("GoalsName") name: String,
           @JsonProperty("createddate") created: Date,
           @JsonProperty("duedate") dueDate: Date?,
           @JsonProperty("time") time: String?,
           @JsonProperty("session") session: String?,
           @JsonProperty("user_id") userId: Int,
           @JsonProperty("ContactID") contactId: String,
           @JsonProperty("ContactName") contactName: String?,
           @JsonProperty("Details") details: String?,
           @JsonProperty("status") status: Int,
           @JsonProperty("updated_datetime") updated: Date?,
           @JsonProperty("allday") allDay: Boolean) : ToDo(id, priorityId, priority, contactId,
        contactName, userId, name, created, updated, dueDate, time, session, status, allDay, details),
        Parcelable {

    @JsonProperty("actions") var actions: ArrayList<ShortValue> = arrayListOf()

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date?,
            parcel.readByte() != 0.toByte()) {
        parcel.readTypedList(actions, ShortValue.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(name)
        parcel.writeSerializable(created)
        parcel.writeSerializable(dueDate)
        parcel.writeString(time)
        parcel.writeString(session)
        parcel.writeInt(userId)
        parcel.writeString(contactId)
        parcel.writeString(contactName)
        parcel.writeString(details)
        parcel.writeInt(status)
        parcel.writeSerializable(updated)
        parcel.writeByte(if (allDay ?: false) 1 else 0)
        parcel.writeTypedList(actions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Goal> {
        override fun createFromParcel(parcel: Parcel): Goal {
            return Goal(parcel)
        }

        override fun newArray(size: Int): Array<Goal?> {
            return arrayOfNulls(size)
        }
    }
}

class TeamNeed(@JsonProperty("NeedsID") id: Int,
               @JsonProperty("priority_id") priorityId: Int,
               @JsonProperty("priority") priority: String?,
               @JsonProperty("ContactID") contactId: String,
               @JsonProperty("ContactName") contactName: String?,
               @JsonProperty("UserID") userId: Int,
               @JsonProperty("NeedsName") name: String,
               @JsonProperty("created_datetime") created: Date,
               @JsonProperty("updated_datetime") updated: Date?,
               @JsonProperty("duedate") dueDate: Date?,
               @JsonProperty("time") time: String?,
               @JsonProperty("session") session: String?,
               @JsonProperty("status") status: Boolean,
               @JsonProperty("allday") allDay: Boolean?,
               @JsonProperty("Details") details: String?): ToDo(id, priorityId, priority, contactId,
        contactName, userId, name, created, updated, dueDate, time, session, if (status) 1 else 0, allDay, details), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date?,
            parcel.readSerializable() as Date?,
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(contactId)
        parcel.writeString(contactName)
        parcel.writeInt(userId)
        parcel.writeString(name)
        parcel.writeSerializable(created)
        parcel.writeSerializable(updated)
        parcel.writeSerializable(dueDate)
        parcel.writeString(time)
        parcel.writeString(session)
        parcel.writeByte(status.toByte())
        parcel.writeValue(allDay)
        parcel.writeString(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeamNeed> {
        override fun createFromParcel(parcel: Parcel): TeamNeed {
            return TeamNeed(parcel)
        }

        override fun newArray(size: Int): Array<TeamNeed?> {
            return arrayOfNulls(size)
        }
    }
}

open class ToDo(val id: Int, val priorityId: Int, val priority: String?, val contactId: String,
           val contactName: String?, val userId: Int, val name: String, val created: Date, val updated: Date?, val dueDate: Date?,
           val time: String?, val session: String?, val status: Int, val allDay: Boolean?, val details: String?): Parcelable {

    var isSelected = false

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date?,
            parcel.readSerializable() as Date?,
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(contactId)
        parcel.writeString(contactName)
        parcel.writeInt(userId)
        parcel.writeString(name)
        parcel.writeSerializable(created)
        parcel.writeSerializable(updated)
        parcel.writeSerializable(dueDate)
        parcel.writeString(time)
        parcel.writeString(session)
        parcel.writeInt(status)
        parcel.writeByte(if (allDay == true) 1 else 0)
        parcel.writeString(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDo> {
        override fun createFromParcel(parcel: Parcel): ToDo {
            return ToDo(parcel)
        }

        override fun newArray(size: Int): Array<ToDo?> {
            return arrayOfNulls(size)
        }
    }
}

class DashContact(@JsonProperty("ContactID") val contactId: Int,
                  @JsonProperty("ContactName") val contactName: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(contactName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DashContact> {
        override fun createFromParcel(parcel: Parcel): DashContact {
            return DashContact(parcel)
        }

        override fun newArray(size: Int): Array<DashContact?> {
            return arrayOfNulls(size)
        }
    }
}

class ShortValue(@JsonProperty("ID") val id: Int,
                 @JsonProperty("Name") val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShortValue> {
        override fun createFromParcel(parcel: Parcel): ShortValue {
            return ShortValue(parcel)
        }

        override fun newArray(size: Int): Array<ShortValue?> {
            return arrayOfNulls(size)
        }
    }
}

class Reachout(@JsonProperty("ReacheoutID") val id: Int,
               @JsonProperty("ReacheoutName") val name: String,
               @JsonProperty("contactdetails") val contactDetails: ReachoutContactDetails): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(ReachoutContactDetails::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeParcelable(contactDetails, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reachout> {
        override fun createFromParcel(parcel: Parcel): Reachout {
            return Reachout(parcel)
        }

        override fun newArray(size: Int): Array<Reachout?> {
            return arrayOfNulls(size)
        }
    }
}

class Followup(@JsonProperty("TouchID") val id: Int,
               @JsonProperty("TouchName") val name: String,
               @JsonProperty("contactdetails") val contactDetails: ReachoutContactDetails): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(ReachoutContactDetails::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeParcelable(contactDetails, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Followup> {
        override fun createFromParcel(parcel: Parcel): Followup {
            return Followup(parcel)
        }

        override fun newArray(size: Int): Array<Followup?> {
            return arrayOfNulls(size)
        }
    }
}

class ReachoutContactDetails(@JsonProperty("ContactID") val contactId: String,
                            @JsonProperty("FirstName") val firstName: String,
                            @JsonProperty("LastName") val lastName: String,
                            @JsonProperty("Userimage") val userImage: String?): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contactId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(userImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReachoutContactDetails> {
        override fun createFromParcel(parcel: Parcel): ReachoutContactDetails {
            return ReachoutContactDetails(parcel)
        }

        override fun newArray(size: Int): Array<ReachoutContactDetails?> {
            return arrayOfNulls(size)
        }
    }
}

enum class TodoType {
    event, action, goal, need;

    fun toInt(): Int = when(this) {
        event -> 0
        action -> 1
        goal -> 2
        need ->3
    }

    fun needCheckBox(): Boolean = when(this) {
        action, goal, need -> true
        event -> false
    }

    fun needDate(): Boolean = when(this) {
        action, goal, need -> false
        event -> true
    }

    fun apiName(): String = when (this) {
        action -> "task"
        goal -> "goal"
        need -> "need"
        event -> "event"
    }

    fun title(): String = when(this) {
        action -> "Action"
        goal -> "Goal"
        need -> "Team Need"
        event -> "Event"
    }

    companion object {
        fun fromInt(intType: Int): TodoType = when(intType) {
            0 -> event
            1 -> action
            2 -> goal
            else -> need
        }
    }
}
