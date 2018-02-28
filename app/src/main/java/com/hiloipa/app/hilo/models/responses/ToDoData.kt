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
class ToDoData {

}

class Event(@JsonProperty("event_id") val eventId: Int,
            @JsonProperty("event_title") val eventTitle: String,
            @JsonProperty("StartDate") val startDate: Date,
            @JsonProperty("StartTime") val startTime: String,
            @JsonProperty("StartSession") val startSession: String,
            @JsonProperty("EndTime") val endTime: String,
            @JsonProperty("EndSession") val endSession: String,
            @JsonProperty("ContactID") val contactId: String,
            @JsonProperty("ContactName") val contactName: String,
            @JsonProperty("Details") val details: String,
            @JsonProperty("location") val location: String,
            @JsonProperty("event_type") val eventType: String,
            @JsonProperty("status") val status: Int,
            @JsonProperty("created_datetime") val created: Date,
            @JsonProperty("updated_datetime") val updated: Date?,
            @JsonProperty("EndDate") val endDate: Date,
            @JsonProperty("allday") val allDay: Boolean): Parcelable {
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
        parcel.writeInt(eventId)
        parcel.writeString(eventTitle)
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
        parcel.writeByte(if (allDay) 1 else 0)
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

class Action(@JsonProperty("TaskID") val taskId: Int,
             @JsonProperty("priority_id") val priorityId: Int,
             @JsonProperty("priority") val priority: String,
             @JsonProperty("TaskName") val taskName: String,
             @JsonProperty("time") val time: String,
             @JsonProperty("session") val session: String,
             @JsonProperty("createddate") val created: Date,
             @JsonProperty("duedate") val dueDate: Date,
             @JsonProperty("user_id") val userId: Int,
             @JsonProperty("ContactID") val contactId: String,
             @JsonProperty("ContactName") val contactName: String,
             @JsonProperty("Details") val details: String,
             @JsonProperty("status") val status: Int,
             @JsonProperty("updated_datetime") val updated: Date?,
             @JsonProperty("allday") val allDay: Boolean): Parcelable {

    @JsonProperty("goal_id") lateinit var goalId: List<ShortValue>

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date,
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
        parcel.writeInt(taskId)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(taskName)
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
        parcel.writeByte(if (allDay) 1 else 0)
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

class Goal(@JsonProperty("GoalsID") val goalId: Int,
           @JsonProperty("priority_id") val priorityId: Int,
           @JsonProperty("priority") val priority: String,
           @JsonProperty("GoalsName") val goalName: String,
           @JsonProperty("createddate") val created: Date,
           @JsonProperty("duedate") val dueDate: Date,
           @JsonProperty("time") val time: String,
           @JsonProperty("session") val session: String,
           @JsonProperty("user_id") val userId: Int,
           @JsonProperty("ContactID") val contactId: String,
           @JsonProperty("ContactName") val contactName: String,
           @JsonProperty("Details") val details: String,
           @JsonProperty("status") val status: Int,
           @JsonProperty("updated_datetime") val updated: Date?,
           @JsonProperty("allday") val allDay: Boolean): Parcelable {

    @JsonProperty("actions") lateinit var actions: ArrayList<ShortValue>

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
        parcel.writeInt(goalId)
        parcel.writeInt(priorityId)
        parcel.writeString(priority)
        parcel.writeString(goalName)
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
        parcel.writeByte(if (allDay) 1 else 0)
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

class ShortValue(@JsonProperty("ID") val id: Int,
                 @JsonProperty("Name") val name: String): Parcelable {
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
