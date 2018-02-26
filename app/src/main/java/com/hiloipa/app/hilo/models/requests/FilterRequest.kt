package com.hiloipa.app.hilo.models.requests

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
class FilterRequest() : StandardRequest(), Parcelable {
    @JsonProperty("Contact_type") var contactType: String = ""
    @JsonProperty("group") var group: String = ""
    @JsonProperty("Pipeline_position") var pipelinePosition: String = ""
    @JsonProperty("Email_unsubscribed") var emailUnsubscribed: String = ""
    @JsonProperty("Product_purchased") var productPurchased: String = ""
    @JsonProperty("Recommended_product") var recommendedProduct: String = ""
    @JsonProperty("Product_interest") var productInterest: String = ""
    @JsonProperty("Solution_tool_recommended") var solutionToolRecommended: String = ""
    @JsonProperty("Solution_tool_yesno") var solutionToolYesNo: String = ""
    @JsonProperty("Concerns") var concerns: String = ""
    @JsonProperty("Gift") var gift: String = ""
    @JsonProperty("Glow") var glow: String = ""
    @JsonProperty("LeadTemp") var leadTem: String = ""
    @JsonProperty("Tags") var tags: String = ""
    @JsonProperty("Three3way") var threeWayCall: String = ""
    @JsonProperty("Derivedby") var derivedBy: String = ""
    @JsonProperty("Source") var source: String = ""
    @JsonProperty("State") var state: String = ""
    @JsonProperty("City") var city: String = ""
    @JsonProperty("Country") var country: String = ""
    @JsonProperty("page") var page: Int = 1

    constructor(parcel: Parcel) : this() {
        contactType = parcel.readString()
        group = parcel.readString()
        pipelinePosition = parcel.readString()
        emailUnsubscribed = parcel.readString()
        productPurchased = parcel.readString()
        recommendedProduct = parcel.readString()
        productInterest = parcel.readString()
        solutionToolRecommended = parcel.readString()
        solutionToolYesNo = parcel.readString()
        concerns = parcel.readString()
        gift = parcel.readString()
        glow = parcel.readString()
        leadTem = parcel.readString()
        tags = parcel.readString()
        threeWayCall = parcel.readString()
        derivedBy = parcel.readString()
        source = parcel.readString()
        state = parcel.readString()
        city = parcel.readString()
        country = parcel.readString()
        page = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contactType)
        parcel.writeString(group)
        parcel.writeString(pipelinePosition)
        parcel.writeString(emailUnsubscribed)
        parcel.writeString(productPurchased)
        parcel.writeString(recommendedProduct)
        parcel.writeString(productInterest)
        parcel.writeString(solutionToolRecommended)
        parcel.writeString(solutionToolYesNo)
        parcel.writeString(concerns)
        parcel.writeString(gift)
        parcel.writeString(glow)
        parcel.writeString(leadTem)
        parcel.writeString(tags)
        parcel.writeString(threeWayCall)
        parcel.writeString(derivedBy)
        parcel.writeString(source)
        parcel.writeString(state)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeInt(page)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterRequest> {
        override fun createFromParcel(parcel: Parcel): FilterRequest {
            return FilterRequest(parcel)
        }

        override fun newArray(size: Int): Array<FilterRequest?> {
            return arrayOfNulls(size)
        }
    }
}