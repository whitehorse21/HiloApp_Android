/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Eduard Albu on 05 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AllProductsResponse(@JsonProperty("Allproducts") val products: ArrayList<Product>,
                          @JsonProperty("Userproducts") val userProducts: ArrayList<Product>)

@JsonIgnoreProperties(ignoreUnknown = true)
class Product(@JsonProperty("bundle_savings") val bundleSavings: Int,
              @JsonProperty("category") val category: String?,
              @JsonProperty("company_id") val companyId: Int,
              @JsonProperty("company_name") val companyName: String,
              @JsonProperty("pc_price") val piecePrice: Int,
              @JsonProperty("product_id") val productId: Int,
              @JsonProperty("product_imgaes") val productImage: String,
              @JsonProperty("product_name") val productName: String,
              @JsonProperty("retail_price") val retailPrice: Int,
              @JsonProperty("updated_datetime") val updated: Date?,
              @JsonProperty("user_id") val userId: Int,
              @JsonProperty("visible") val visible: String?,
              @JsonProperty("wholesale_price") val wholeSalePrice: Int): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readSerializable() as Date?,
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bundleSavings)
        parcel.writeString(category)
        parcel.writeInt(companyId)
        parcel.writeString(companyName)
        parcel.writeInt(piecePrice)
        parcel.writeInt(productId)
        parcel.writeString(productImage)
        parcel.writeString(productName)
        parcel.writeInt(retailPrice)
        parcel.writeSerializable(updated)
        parcel.writeInt(userId)
        parcel.writeString(visible)
        parcel.writeInt(wholeSalePrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
