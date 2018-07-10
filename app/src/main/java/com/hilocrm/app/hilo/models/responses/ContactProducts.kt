/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by Eduard Albu on 07 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class ContactProducts(@JsonProperty("ProductInterested") val interested: ArrayList<ContactProduct>,
                      @JsonProperty("RecommendedProduct") val recommended: ArrayList<ContactProduct>,
                      @JsonProperty("SolutionToolRecommended") val solutionTool: ArrayList<ContactProduct>?,
                      @JsonProperty("ProductPurchased") val purchased: ArrayList<ContactProduct>,
                      @JsonProperty("Timestamp") val timestamp: Date)

class ContactProduct(@JsonProperty("productid") val id: Int,
              @JsonProperty("assinged_type") val assignedType: String,
              @JsonProperty("gift") val gift: String?,
              @JsonProperty("isminifacialgiven") val isMiniFacial: Boolean,
              @JsonProperty("product_name") val name: String,
              @JsonProperty("product_imgaes") val productImage: String,
              @JsonProperty("user_id") val userId: String?): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(assignedType)
        parcel.writeString(gift)
        parcel.writeByte(if (isMiniFacial) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(productImage)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactProduct> {
        override fun createFromParcel(parcel: Parcel): ContactProduct {
            return ContactProduct(parcel)
        }

        override fun newArray(size: Int): Array<ContactProduct?> {
            return arrayOfNulls(size)
        }
    }
}