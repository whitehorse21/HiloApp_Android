package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 18.02.2018.
 */
class SignUpResponse(): Parcelable {

    @JsonProperty("hear_list") lateinit var hears: ArrayList<HearName>
    @JsonProperty("company_list") lateinit var companies: ArrayList<CompanyName>

    constructor(parcel: Parcel) : this() {
        parcel.readTypedList(hears, HearName.CREATOR)
        parcel.readTypedList(companies, CompanyName.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(hears)
        parcel.writeTypedList(companies)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SignUpResponse> {
        override fun createFromParcel(parcel: Parcel): SignUpResponse {
            return SignUpResponse(parcel)
        }

        override fun newArray(size: Int): Array<SignUpResponse?> {
            return arrayOfNulls(size)
        }
    }
}

class CompanyName(@JsonProperty("companyid") val companyId: Int,
                   @JsonProperty("companyname") val companyName: String): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(companyId)
        parcel.writeString(companyName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompanyName> {
        override fun createFromParcel(parcel: Parcel): CompanyName {
            return CompanyName(parcel)
        }

        override fun newArray(size: Int): Array<CompanyName?> {
            return arrayOfNulls(size)
        }
    }
}

class HearName(@JsonProperty("hearid") val hearId: String,
               @JsonProperty("hearname") val hearName: String): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hearId)
        parcel.writeString(hearName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HearName> {
        override fun createFromParcel(parcel: Parcel): HearName {
            return HearName(parcel)
        }

        override fun newArray(size: Int): Array<HearName?> {
            return arrayOfNulls(size)
        }
    }
}