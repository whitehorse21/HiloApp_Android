package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by eduardalbu on 27.02.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class NewContactData {
    @JsonProperty("title") var title: String? = null
    @JsonProperty("TitleDropValue") lateinit var titles: ArrayList<DropDownValue>
    @JsonProperty("FirstName") var firstName: String? = null
    @JsonProperty("LastName") var lastName: String? = null
    @JsonProperty("Email") var email: String? = null
    @JsonProperty("AlternativeEmail") var alternEmail: String? = null
    @JsonProperty("Phonenumber") var phoneNumber: String? = null
    @JsonProperty("alternatephns") var alternPhones: String? = null
    @JsonProperty("Groups") var groups: String? = null
    @JsonProperty("GroupDropValue") lateinit var groupsList: ArrayList<DropDownValue>
    @JsonProperty("ContactType") var contactType: String? = null
    @JsonProperty("ContactTypeDropValue") lateinit var contactTypes: ArrayList<DropDownValue>
    @JsonProperty("PipelinePosition") var pipelinePosition: String? = null
    @JsonProperty("PipelineDropValue") lateinit var pipelines: ArrayList<DropDownValue>
    @JsonProperty("temp") var temp: String? = null
    @JsonProperty("TempDropValue") lateinit var temps: ArrayList<DropDownValue>
    @JsonProperty("children") var children: String? = null
    @JsonProperty("spouse") var spouse: String? = null
    @JsonProperty("birthday_day") var birthDateDay: String? = null
    @JsonProperty("BirthdayDropValue") lateinit var birthDateDays: ArrayList<DropDownValue>
    @JsonProperty("birthday_month") var birthDateMonth: String? = null
    @JsonProperty("BirthdaymonthDropValue") lateinit var birthDateMonths: ArrayList<DropDownValue>
    @JsonProperty("thereway_call") lateinit var thereWayCall: String
    @JsonProperty("attender") var attender: String? = null
    @JsonProperty("derivedby") var derivedBy: String? = null
    @JsonProperty("DerivedByDropValue") lateinit var derivedBys: ArrayList<DropDownValue>
    @JsonProperty("otherderivedby") var otherDerivedBy: String? = null
    @JsonProperty("company") var company: String? = null
    @JsonProperty("addjob") var addJob: String? = null
    @JsonProperty("gift") lateinit var gift: String
    @JsonProperty("mini_facial") lateinit var glowSample: String
    @JsonProperty("ShipDateTime") var shipDateTime: Date? = null
    @JsonProperty("giftgiven") var giftGiven: String? = null
    @JsonProperty("chk_wrink") var wrinkless: Boolean = false
    @JsonProperty("chk_sogg") var sogg: Boolean = false
    @JsonProperty("chk_sensit") var sensit: Boolean = false
    @JsonProperty("chk_acne") var acne: Boolean = false
    @JsonProperty("chk_darkmark") var darkMark: Boolean = false
    @JsonProperty("chk_dullness") var dullness: Boolean = false
    @JsonProperty("chk_lash_brows") var lashBrows: Boolean = false
    @JsonProperty("AssignTags") var assignedTags: String? = null
    @JsonProperty("NewTag") var newTag: String? = null
    @JsonProperty("AssignCustomFields") lateinit var assignCustomFields: ArrayList<CustomField>
    @JsonProperty("NewCustomFields") var newCustomFields: String? = null
    @JsonProperty("Facebook_Personal") var facebookPersonal: String? = null
    @JsonProperty("Facebook_Business") var facebookBusiness: String? = null
    @JsonProperty("Twitter") var twitter: String? = null
    @JsonProperty("Instagram") var instagram: String? = null
    @JsonProperty("Pinterest") var pinterest: String? = null
    @JsonProperty("Persicope") var periscope: String? = null
    @JsonProperty("LinkedIn") var linkedIn: String? = null
    @JsonProperty("Snap_Chat") var snapChat: String? = null
    @JsonProperty("Vine") var vine: String? = null
    @JsonProperty("Tumblr") var tumblr: String? = null
    @JsonProperty("YouTube") var youTube: String? = null
    @JsonProperty("Google_Plus") var googlePlus: String? = null
    @JsonProperty("Meetup") var meetUp: String? = null
    @JsonProperty("Website_Personal") var personalWebsite: String? = null
    @JsonProperty("Website_Business") var businessWebsite: String? = null
    @JsonProperty("Other_1") var other1: String? = null
    @JsonProperty("Other_2") var other2: String? = null
    @JsonProperty("Street") var street: String? = null
    @JsonProperty("Street2") var street2: String? = null
    @JsonProperty("City") var city: String? = null
    @JsonProperty("State") var state: String? = null
    @JsonProperty("statedropValue") lateinit var states: ArrayList<DropDownValue>
    @JsonProperty("Zipcode") val zipCode: String? = null
    @JsonProperty("Country") val country: String? = null
    @JsonProperty("CountryList") lateinit var countries: ArrayList<DropDownValue>
    @JsonProperty("AllTags") lateinit var allTags: String
    @JsonProperty("AllCustomFields") lateinit var allCustomFields: ArrayList<CustomField>
    @JsonProperty("CompanyName") lateinit var companyName: String
    @JsonProperty("SourceDropValueDrop") lateinit var sources: ArrayList<DropDownValue>
}