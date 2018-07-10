package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 22.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class SearchContact(@JsonProperty("ContactID") id: Int,
                    @JsonProperty("ContactName") name: String): Contact(id, name, 0, null), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
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
