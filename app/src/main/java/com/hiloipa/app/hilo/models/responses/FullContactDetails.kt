package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by eduardalbu on 23.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FullContactDetails(@JsonProperty("AllCustomField") var customFields: ArrayList<CustomField>,
                         @JsonProperty("AllTags") var tags: String,
                         @JsonProperty("contactdetails") val contactDetails: ContactDetails)

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomField(@JsonProperty("FieldName") var fieldName: String,
                  @JsonProperty("FieldValue") var fieldValue: String)

@JsonIgnoreProperties(ignoreUnknown = true)
class ContactDetails(@JsonProperty("AlternativeEmail") val alternativeEmail: String,
                     @JsonProperty("AssignCustomFields") val assignCustomFields: ArrayList<CustomField>,
                     @JsonProperty("City") val city: String?,
                     @JsonProperty("ContactID") val contactId: Int,
                     @JsonProperty("ContactNumber") val contactNumber: String,
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
                     @JsonProperty("alternatephns") val alternatephns: String,
                     @JsonProperty("attender") val attender: String,
                     @JsonProperty("birthday") val birthday: Date?,
                     @JsonProperty("birthday_day") val birthdayDay: Int,
                     @JsonProperty("birthday_month") val birthdayMonth: String,
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
                     @JsonProperty("updated_datetime") val updatedDateTime: Date,
                     @JsonProperty("created_datetime") val createdDateTime: Date)
