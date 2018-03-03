package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 26.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FilterData(@JsonProperty("ContactType") val contactTypes: ArrayList<FilterValue>,
                 @JsonProperty("Contact_Tag") val contactTags: ArrayList<FilterValue>,
                 @JsonProperty("Groups") val contactGroups: ArrayList<FilterValue>,
                 @JsonProperty("PipelinePosition") val contactPipelines: ArrayList<FilterValue>,
                 @JsonProperty("ProductInterest") val contactProductsInterests: ArrayList<FilterValue>,
                 @JsonProperty("ProductPurchased") val contactPurhcasedProducts: ArrayList<FilterValue>,
                 @JsonProperty("RecommendedProduct") val contactRecommendedProducts: ArrayList<FilterValue>,
                 @JsonProperty("SolutionToolRecommended") val contactRecommendedSolutionTools: ArrayList<FilterValue>,
                 @JsonProperty("SolutionToolYesNo") val solutionToolsYesNo: ArrayList<FilterValue>,
                 @JsonProperty("State") val contactStates: ArrayList<FilterValue>,
                 @JsonProperty("concerns") val contactConcerns: ArrayList<FilterValue>,
                 @JsonProperty("contact_source") val contactSources: ArrayList<FilterValue>,
                 @JsonProperty("derivedby") val derivedBy: ArrayList<FilterValue>,
                 @JsonProperty("gift") val contactGifts: ArrayList<FilterValue>,
                 @JsonProperty("mini_facial") val miniFacial: ArrayList<FilterValue>,
                 @JsonProperty("temp") val contactTemp: ArrayList<FilterValue>,
                 @JsonProperty("thereway_call") val thereWayCall: ArrayList<FilterValue>,
                 @JsonProperty("unsubscripe") val unsubscribe: ArrayList<FilterValue>)

@JsonIgnoreProperties(ignoreUnknown = true)
class FilterValue(@JsonProperty("Text") val text: String,
                  @JsonProperty("Value") val value: String): Parcelable {

    var isSelected: Boolean = true
    var id: Int = -1

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {

        isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(value)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterValue> {
        override fun createFromParcel(parcel: Parcel): FilterValue {
            return FilterValue(parcel)
        }

        override fun newArray(size: Int): Array<FilterValue?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is FilterValue && other.value == value
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}