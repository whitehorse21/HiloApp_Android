package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 22.02.2018.
 */
class SearchContact(@JsonProperty("ContactID") val contactId: Int,
                    @JsonProperty("ContactName") val name: String): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(contactId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchContact> {
        override fun createFromParcel(parcel: Parcel): SearchContact {
            return SearchContact(parcel)
        }

        override fun newArray(size: Int): Array<SearchContact?> {
            return arrayOfNulls(size)
        }
    }
}
