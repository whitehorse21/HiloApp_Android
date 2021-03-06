/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.models.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class SaveContactRequest: StandardRequest() {
    @JsonProperty("FirstName") var firstName: String = ""
    @JsonProperty("LastName") var lastName: String = ""
    @JsonProperty("Street") var street: String = ""
    @JsonProperty("Street2") var street2: String = ""
    @JsonProperty("City") var city: String = ""
    @JsonProperty("State") var state: String = ""
    @JsonProperty("Zipcode") var zipCode: String = ""
    @JsonProperty("Country") var country: String = ""
    @JsonProperty("Email") var email: String = ""
    @JsonProperty("ContactNumber") var phoneNumber: String = ""
    @JsonProperty("Pipeline") var pipeline: String = ""
    @JsonProperty("Pipeline_id") var pipelineId: String = ""
    @JsonProperty("ContactType") var contactType: String = ""
    @JsonProperty("AssignTags") var assignedTags: String = ""
    @JsonProperty("NewTag") var newTag: String = ""
    @JsonProperty("Groups") var groups: String = ""
    @JsonProperty("ShipDateTime") var shipDateTime: String = ""
    @JsonProperty("gift") var gift: String = "true"
    @JsonProperty("mini_facial") var glowSample: String = "false"
    @JsonProperty("thereway_call") var thereWayCall: String = "false"
    @JsonProperty("Contact_Tag") var contactTag: String = ""
    @JsonProperty("company") var company: String = ""
    @JsonProperty("AssignCustomFields") var assignCustomFields: String = ""
    @JsonProperty("NewCustomFields") var newCustomFields: String = ""
    @JsonProperty("customvalue") var customValue: String = ""
    @JsonProperty("addjob") var addJob: String = ""
    @JsonProperty("chk_wrink") var wrinkless: String = "false"
    @JsonProperty("chk_sogg") var sogg: String = "false"
    @JsonProperty("chk_sensit") var sensit: String = "false"
    @JsonProperty("chk_acne") var acne: String = "false"
    @JsonProperty("chk_darkmark") var darkMark: String = "false"
    @JsonProperty("chk_dullness") var dullness: String = "false"
    @JsonProperty("chk_lash_brows") var lashBrows: String = "false"
    @JsonProperty("Facebook_Personal") var facebookPersonal: String = ""
    @JsonProperty("Facebook_Business") var facebookBusiness: String = ""
    @JsonProperty("Twitter") var twitter: String = ""
    @JsonProperty("Instagram") var instagram: String = ""
    @JsonProperty("Pinterest") var pinterest: String = ""
    @JsonProperty("Persicope") var periscope: String = ""
    @JsonProperty("LinkedIn") var linkedIn: String = ""
    @JsonProperty("Snap_Chat") var snapChat: String = ""
    @JsonProperty("Vine") var vine: String = ""
    @JsonProperty("Tumblr") var tumblr: String = ""
    @JsonProperty("YouTube") var youTube: String = ""
    @JsonProperty("Google_Plus") var googlePlus: String = ""
    @JsonProperty("Meetup") var meetUp: String = ""
    @JsonProperty("Website_Personal") var personalWebsite: String = ""
    @JsonProperty("Website_Business") var businessWebsite: String = ""
    @JsonProperty("Other_1") var other1: String = ""
    @JsonProperty("Other_2") var other2: String = ""
    @JsonProperty("birthday_day") var birthDateDay: String = ""
    @JsonProperty("birthday_month") var birthDateMonth: String = ""
    @JsonProperty("children") var children: String = ""
    @JsonProperty("spouse") var spouse: String = ""
    @JsonProperty("no_row_message") var noRowMessage: String = ""
    @JsonProperty("derivedby") var derivedBy: String = ""
    @JsonProperty("otherderivedby") var otherDerivedBy: String = ""
    @JsonProperty("giftgiven") var giftGiven: String = ""
    @JsonProperty("chk_update_mode") var checkUpdateMode: String = ""
    @JsonProperty("source_string") var source: String = ""
    @JsonProperty("temp") var temp: String = ""
    @JsonProperty("CountryID") var countryId: String = ""
    @JsonProperty("stateid") var stateId: String = "1"
    @JsonProperty("PipelinePosition") var pipelinePosition: String = ""
    @JsonProperty("title") var title: String = ""
    @JsonProperty("attender") var attender: String = ""
    @JsonProperty("AlternativeEmail") var alternEmail: String = ""
    @JsonProperty("alternatephns") var alternPhones: String = ""
}