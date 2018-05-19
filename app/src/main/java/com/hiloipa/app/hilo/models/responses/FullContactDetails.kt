package com.hiloipa.app.hilo.models.responses

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by eduardalbu on 23.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FullContactDetails(@JsonProperty("AllTags") var tags: String,
                         @JsonProperty("contactdetails") val contactDetails: ContactDetails): Parcelable {

    @JsonProperty("AllCustomField") lateinit var customFields: ArrayList<CustomField>

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(ContactDetails::class.java.classLoader)) {
        parcel.readTypedList(customFields, CustomField.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tags)
        parcel.writeParcelable(contactDetails, flags)
        parcel.writeTypedList(customFields)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FullContactDetails> {
        override fun createFromParcel(parcel: Parcel): FullContactDetails {
            return FullContactDetails(parcel)
        }

        override fun newArray(size: Int): Array<FullContactDetails?> {
            return arrayOfNulls(size)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomField(@JsonProperty("FieldName") var fieldName: String,
                  @JsonProperty("FieldValue") var fieldValue: String): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fieldName)
        parcel.writeString(fieldValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomField> {
        override fun createFromParcel(parcel: Parcel): CustomField {
            return CustomField(parcel)
        }

        override fun newArray(size: Int): Array<CustomField?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is CustomField && other.fieldName == fieldName
    }
}

//@JsonIgnoreProperties(ignoreUnknown = true)
class ContactDetails(@JsonProperty("AlternativeEmail") val alternativeEmail: String?,
                     @JsonProperty("City") val city: String?,
                     @JsonProperty("ContactID") val contactId: Int,
                     @JsonProperty("ContactNumber") val contactNumber: String?,
                     @JsonProperty("ContactType") val contactType: String?,
                     @JsonProperty("ContactType_Position") val contactTypePosition: Int,
                     @JsonProperty("Contact_Tag") val contactTag: String?,
                     @JsonProperty("Country") val country: String?,
                     @JsonProperty("Email") val email: String?,
                     @JsonProperty("Facebook_Business") val facebookBusiness: String?,
                     @JsonProperty("Facebook_Personal") val facebookPersonal: String?,
                     @JsonProperty("FirstName") val firstName: String,
                     @JsonProperty("Google_Plus") val googlePlus: String?,
                     @JsonProperty("Groups") val groups: String?,
                     @JsonProperty("Groups_Position") val groupsPosition: Int,
                     @JsonProperty("Instagram") val instagram: String?,
                     @JsonProperty("LastName") val lastName: String,
                     @JsonProperty("LinkedIn") val linkedIn: String?,
                     @JsonProperty("Meetup") val meetUp: String?,
                     @JsonProperty("Other_1") val other1: String?,
                     @JsonProperty("Other_2") val other2: String?,
                     @JsonProperty("Persicope") val periscope: String?,
                     @JsonProperty("Pinterest") val pinterest: String?,
                     @JsonProperty("Pipeline") val pipeline: Int,
                     @JsonProperty("PipelinePosition") val pipelinePosition: String,
                     @JsonProperty("ProductInterest") val productInterest: String?,
                     @JsonProperty("ProductPurchased") val productPurchased: String?,
                     @JsonProperty("RecommendedProduct") val recommendedProduct: String?,
                     @JsonProperty("ShipDateTime") val shipDateTime: Date?,
                     @JsonProperty("Snap_Chat") val snapChat: String?,
                     @JsonProperty("SolutionToolRecommended") val solutionToolRecommended: String?,
                     @JsonProperty("SolutionToolYesNo") val isSolutionToolRecommended: Boolean,
                     @JsonProperty("State") val state: String?,
                     @JsonProperty("Street") val street: String?,
                     @JsonProperty("Street2") val street2: String?,
                     @JsonProperty("Tumblr") val tumblr: String?,
                     @JsonProperty("Twitter") val twitter: String?,
                     @JsonProperty("Userimage") val userImage: String?,
                     @JsonProperty("Vine") val vine: String?,
                     @JsonProperty("Website_Business") val websiteBusiness: String?,
                     @JsonProperty("Website_Personal") val websitePersonal: String?,
                     @JsonProperty("YouTube") val youTube: String?,
                     @JsonProperty("Zipcode") val zipCode: String?,
                     @JsonProperty("alternatephns") val alternatephns: String?,
                     @JsonProperty("attender") val attender: String?,
                     @JsonProperty("birthday") val birthday: Date?,
                     @JsonProperty("birthday_day") val birthdayDay: Int,
                     @JsonProperty("birthday_month") val birthdayMonth: String?,
                     @JsonProperty("children") val children: String?,
                     @JsonProperty("company") val company: String?,
                     @JsonProperty("concerns") val concerns: String,
                     @JsonProperty("contact_source") val contactSource: String,
                     @JsonProperty("derivedby") val derivedBy: String?,
                     @JsonProperty("follow_reminder") val followReminder: Date?,
                     @JsonProperty("gift") val gift: String,
                     @JsonProperty("giftgiven") val giftGiven: String?,
                     @JsonProperty("is_followup") val isFollowUp: Boolean,
                     @JsonProperty("is_reachout") val isReachOut: Boolean,
                     @JsonProperty("job_title") val jobTitle: String?,
                     @JsonProperty("last_followupdate") val lastFollowUpDate: String?,
                     @JsonProperty("last_reachedout") val lastReachedOut: String?,
                     @JsonProperty("mini_facial") val miniFacial: String,
                     @JsonProperty("otherderivedby") val otherDerivedBy: String?,
                     @JsonProperty("reachfollow_source") val reachFollowSource: String?,
                     @JsonProperty("spouse") val spouse: String?,
                     @JsonProperty("status") val status: Int,
                     @JsonProperty("temp") val temp: String,
                     @JsonProperty("temp_id") val tempId: Int,
                     @JsonProperty("thereway_call") val thereWayToCall: String,
                     @JsonProperty("title") val title: String?,
                     @JsonProperty("unsubscripe") val unsubscribe: String,
                     @JsonProperty("updated_datetime") val updatedDateTime: Date?,
                     @JsonProperty("created_datetime") val createdDateTime: Date): Parcelable {

    @JsonProperty("AssignCustomFields") lateinit var assignCustomFields: ArrayList<CustomField>

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date?,
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as Date,
            parcel.readSerializable() as Date) {

        parcel.readTypedList(assignCustomFields, CustomField.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alternativeEmail)
        parcel.writeString(city)
        parcel.writeInt(contactId)
        parcel.writeString(contactNumber)
        parcel.writeString(contactType)
        parcel.writeInt(contactTypePosition)
        parcel.writeString(contactTag)
        parcel.writeString(country)
        parcel.writeString(email)
        parcel.writeString(facebookBusiness)
        parcel.writeString(facebookPersonal)
        parcel.writeString(firstName)
        parcel.writeString(googlePlus)
        parcel.writeString(groups)
        parcel.writeInt(groupsPosition)
        parcel.writeString(instagram)
        parcel.writeString(lastName)
        parcel.writeString(linkedIn)
        parcel.writeString(meetUp)
        parcel.writeString(other1)
        parcel.writeString(other2)
        parcel.writeString(periscope)
        parcel.writeString(pinterest)
        parcel.writeInt(pipeline)
        parcel.writeString(pipelinePosition)
        parcel.writeString(productInterest)
        parcel.writeString(productPurchased)
        parcel.writeString(recommendedProduct)
        parcel.writeSerializable(shipDateTime)
        parcel.writeString(snapChat)
        parcel.writeString(solutionToolRecommended)
        parcel.writeByte(if (isSolutionToolRecommended) 1 else 0)
        parcel.writeString(state)
        parcel.writeString(street)
        parcel.writeString(street2)
        parcel.writeString(tumblr)
        parcel.writeString(twitter)
        parcel.writeString(userImage)
        parcel.writeString(vine)
        parcel.writeString(websiteBusiness)
        parcel.writeString(websitePersonal)
        parcel.writeString(youTube)
        parcel.writeString(zipCode)
        parcel.writeString(alternatephns)
        parcel.writeString(attender)
        parcel.writeSerializable(birthday)
        parcel.writeInt(birthdayDay)
        parcel.writeString(birthdayMonth)
        parcel.writeString(children)
        parcel.writeString(company)
        parcel.writeString(concerns)
        parcel.writeString(contactSource)
        parcel.writeString(derivedBy)
        parcel.writeSerializable(followReminder)
        parcel.writeString(gift)
        parcel.writeString(giftGiven)
        parcel.writeByte(if (isFollowUp) 1 else 0)
        parcel.writeByte(if (isReachOut) 1 else 0)
        parcel.writeString(jobTitle)
        parcel.writeString(lastFollowUpDate)
        parcel.writeString(lastReachedOut)
        parcel.writeString(miniFacial)
        parcel.writeString(otherDerivedBy)
        parcel.writeString(reachFollowSource)
        parcel.writeString(spouse)
        parcel.writeInt(status)
        parcel.writeString(temp)
        parcel.writeInt(tempId)
        parcel.writeString(thereWayToCall)
        parcel.writeString(title)
        parcel.writeString(unsubscribe)
        parcel.writeSerializable(updatedDateTime)
        parcel.writeSerializable(createdDateTime)
        parcel.writeTypedList(assignCustomFields)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactDetails> {
        override fun createFromParcel(parcel: Parcel): ContactDetails {
            return ContactDetails(parcel)
        }

        override fun newArray(size: Int): Array<ContactDetails?> {
            return arrayOfNulls(size)
        }
    }
}
