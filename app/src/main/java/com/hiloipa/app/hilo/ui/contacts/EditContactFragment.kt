package com.hiloipa.app.hilo.ui.contacts


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TagsSpinnerAdapter
import com.hiloipa.app.hilo.models.Tag
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.CustomField
import com.hiloipa.app.hilo.models.responses.FullContactDetails
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.widget.CustomFieldView
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.edit_address.*
import kotlinx.android.synthetic.main.edit_contact_info.*
import kotlinx.android.synthetic.main.edit_personal_info.*
import kotlinx.android.synthetic.main.edit_rodan_plus_fields.*
import kotlinx.android.synthetic.main.edit_social_and_websites.*
import kotlinx.android.synthetic.main.edit_tags_and_custom_fields.*
import kotlinx.android.synthetic.main.fragment_edit_contact.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class EditContactFragment : Fragment(), View.OnClickListener {

    var contactDetails: FullContactDetails? = null
    var customFields: MutableList<CustomFieldView> = mutableListOf()
    var giftsGiven: MutableList<CustomFieldView> = mutableListOf()

    companion object {
        fun newInstance(contactId: String? = null): EditContactFragment {
            val args = Bundle()
            if (contactId != null)
                args.putString("contactId", contactId)
            val fragment = EditContactFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_edit_contact, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get details from arguments if this is the case
        if (arguments.containsKey("contactId")) {
            val contactId = arguments.getString("contactId")
            getFullContactDetails(contactId)
        }
        // set main buttons click listener
        contactInfoToggleBtn.setOnClickListener(this)
        personalInfoToggleBtn.setOnClickListener(this)
        rodanFieldsToggleBtn.setOnClickListener(this)
        tagsAndFieldsToggleBtn.setOnClickListener(this)
        socialAndWebsitesToggleBtn.setOnClickListener(this)
        addressToggleBtn.setOnClickListener(this)
    }

    private fun getFullContactDetails(contactId: String) {
        val request = StandardRequest()
        request.contactId = contactId

        val loading = activity.showLoading()
        HiloApp.api().getContactFullDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FullContactDetails> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.contactDetails = data
                            this.updateUIWithNewDetails(data)
                        }
                    } else {
                        activity.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateUIWithNewDetails(contactDetails: FullContactDetails) {
        val details = contactDetails.contactDetails
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        // contact info
        titleField.text = details.title
        firstNameField.setText(details.firstName)
        lastNameField.setText(details.lastName)
        emailField.setText(details.email)
        emailField1.setText(details.alternativeEmail)
        phoneNumberField.setText(details.contactNumber)
        val otherPhones = details.alternatephns.split(",")
        otherPhones.forEach {
            if (!it.equals("empty", true)) {
                if (cellPhoneField.text.isEmpty()) {
                    cellPhoneField.setText(it)
                    return@forEach
                }

                if (workPhoneField.text.isEmpty()) {
                    workPhoneField.setText(it)
                    return@forEach
                }

                if (homePhoneField.text.isEmpty()) {
                    homePhoneField.setText(it)
                    return@forEach
                }
            }
        }

        // personal info
        groupButton.text = details.groups
        contactTypeButton.text = details.contactType
        pipelinePositionButton.text = details.pipelinePosition
        tempButton.text = details.temp
        childrenField.setText(details.children)
        spouseField.setText(details.spouse)
        // setup birth date button
        var birthDate = details.birthday
        birthDayButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                birthDate = Date(calendar.timeInMillis)
                birthDayButton.text = dateFormat.format(birthDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
        // set seleceted date in the button if it is not null
        if (birthDate != null)
            birthDayButton.text = dateFormat.format(birthDate)
        threeWayCallButton.text = details.thereWayToCall
        derivedByButton.text = details.derivedBy
        companyField.setText(details.company)
        sourceField.setText(details.contactSource)
        jobTitleField.setText(details.jobTitle)

        // rodan + fields
        if (HiloApp.userData.userCompany.equals("rodan + fields", true)) {
            rodanFieldsRootLayout.visibility = View.VISIBLE
            giftYesBtn.isChecked = details.gift.equals("yes", true)
            giftNoBtn.isChecked = details.gift.equals("no", true)
            // setup add gift button to add a new gift when it is clicked
            addGiftBtn.setOnClickListener {
                val gift = giftGivenField.text.toString()
                if (gift.isNotEmpty()) {
                    val giftField = CustomField(getString(R.string.gift_given), gift)
                    val customFieldView = CustomFieldView(activity, giftField)
                    customFieldView.deleteClickListener(onDeleteGiftListener)
                    this.giftsGiven.add(customFieldView)
                    giftFieldsContainer.addView(customFieldView)
                    giftGivenField.setText("")
                }
            }

            // check if we have any added gifts
            val giftsGiven = details.giftGiven
            if (giftsGiven != null) {
                // split gifts by coma into a list
                val gifts = giftsGiven.split(",")
                gifts.forEach {
                    // add a field for each gift
                    if (it.isNotEmpty()) {
                        val giftField = CustomField(getString(R.string.gift_given), it)
                        val customFieldView = CustomFieldView(activity, giftField)
                        customFieldView.deleteClickListener(onDeleteGiftListener)
                        this.giftsGiven.add(customFieldView)
                        giftFieldsContainer.addView(customFieldView)
                    }
                }
            }

            // setup concerns check boxes
            val concernsList = details.concerns.split(",")
            val checkBoxes = listOf(wrinklesCheckBox, sensitivityCheckBox, darkMarksCheckBox,
                    lashesBrowsCheckBox, saggingCheckBox, acneCheckBox, dullnessCheckBox)
            if (concernsList.isNotEmpty()) {
                checkBoxes.forEach {
                    if (concernsList.contains(it.text.toString().toLowerCase())) it.isChecked = true
                }
            }

            // auto ship date field
            var autoShipDate = details.shipDateTime
            autoShipDayButton.setOnClickListener {
                val calendar = Calendar.getInstance()
                val datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    autoShipDate = Date(calendar.timeInMillis)
                    autoShipDayButton.text = dateFormat.format(autoShipDate)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
            if (autoShipDate != null) {
                autoShipDayButton.text = dateFormat.format(autoShipDate)
            }

            // setup glow sample buttons
            enableGlowSampleBtn.isChecked = details.miniFacial.equals("yes", true)
            disableGlowSampleBtn.isChecked = details.miniFacial.equals("no", true)
        } else rodanFieldsRootLayout.visibility = View.GONE

        // tags and custom fields
        details.assignCustomFields.forEach { field ->
            val fieldInput = CustomFieldView(activity, field)
            fieldInput.deleteClickListener(onDeleteFieldListener)
            this.customFields.add(fieldInput)
            customFieldsLayout.addView(fieldInput)
        }
        tagsButton.text = details.contactTag
        val tagsNames = contactDetails.tags.split(",")
        val tags = arrayListOf<Tag>()
        tagsNames.forEach { tags.add(Tag(name = it)) }
        var isFromUser = false
        val adapter = TagsSpinnerAdapter(tags = tags)
        adapter.delegate = object : TagsSpinnerAdapter.TagSpinnerDelegate {
            override fun didClickOnTag(tag: Tag, position: Int) {
                var currentText = tagsButton.text.toString()
                if (currentText.isNotEmpty() && tag.isSelected)
                    currentText = "$currentText,${tag.name}"
                else if (tag.isSelected)
                    currentText = tag.name
                else {
                    val regex = ",*(${tag.name})(,$)*".toRegex()
                    currentText = regex.replace(currentText, "")
                }

                tagsButton.text = currentText
            }
        }
        tagsSpinner.adapter = adapter
        tagsButton.setOnClickListener {
            isFromUser = true
            tagsSpinner.performClick()
        }
        addFieldBtn.setOnClickListener {
            val fieldHint = newCustomField.text.toString()
            if (fieldHint.isNotEmpty()) {
                val customField = CustomField(fieldHint, "")
                val customFieldView = CustomFieldView(activity, customField)
                customFieldView.deleteClickListener(onDeleteFieldListener)
                this.customFields.add(customFieldView)
                customFieldsLayout.addView(customFieldView)
                newCustomField.setText("")
                activity.hideKeyboard()
            }
        }

        // social and websites
        facebookBusinessField.setText(details.facebookBusiness)
        facebookPersonalField.setText(details.facebookPersonal)
        twitterField.setText(details.twitter)
        instagramField.setText(details.instagram)
        pinterestField.setText(details.pinterest)
        periscopeField.setText(details.periscope)
        linkedInField.setText(details.linkedIn)
        snapChatField.setText(details.snapChat)
        vineField.setText(details.vine)
        tumblrField.setText(details.tumblr)
        youTubeField.setText(details.youTube)
        googlePlusField.setText(details.googlePlus)
        meetupField.setText(details.meetUp)
        websiteBusinessField.setText(details.websiteBusiness)
        websitePersonalField.setText(details.websitePersonal)
        other1Field.setText(details.other1)
        other2Field.setText(details.other2)

        // address
        streetField.setText(details.street)
        street2Field.setText(details.street2)
        cityField.setText(details.city)
        stateField.setText(details.state)
        zipCodeField.setText(details.zipCode)
        countryField.setText(details.country)
    }

    private val onDeleteFieldListener = View.OnClickListener { v ->
        val field: CustomFieldView = customFieldsLayout.findViewWithTag(v.tag)
        customFieldsLayout.removeView(field)
        customFields.remove(field)
    }

    private val onDeleteGiftListener = View.OnClickListener { v ->
        val field: CustomFieldView = giftFieldsContainer.findViewWithTag(v.tag)
        giftFieldsContainer.removeView(field)
        giftsGiven.remove(field)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.contactInfoToggleBtn -> {
                contactInfoLayout.visibility =
                        if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                contactInfoToggleBtn.isSelected =
                        contactInfoLayout.visibility == View.VISIBLE
            }
            R.id.personalInfoToggleBtn -> {
                personalInfoLayout.visibility =
                        if (personalInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                personalInfoToggleBtn.isSelected =
                        personalInfoLayout.visibility == View.VISIBLE
            }
            R.id.rodanFieldsToggleBtn -> {
                rodanFieldsLayout.visibility =
                        if (rodanFieldsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                rodanFieldsToggleBtn.isSelected =
                        rodanFieldsLayout.visibility == View.VISIBLE
            }
            R.id.tagsAndFieldsToggleBtn -> {
                tagsAndFieldsLayout.visibility =
                        if (tagsAndFieldsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                tagsAndFieldsToggleBtn.isSelected =
                        tagsAndFieldsLayout.visibility == View.VISIBLE
            }
            R.id.socialAndWebsitesToggleBtn -> {
                socialAndWebsitesLayout.visibility =
                        if (socialAndWebsitesLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                socialAndWebsitesToggleBtn.isSelected =
                        socialAndWebsitesLayout.visibility == View.VISIBLE
            }
            R.id.addressToggleBtn -> {
                addressLayout.visibility =
                        if (addressLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                addressToggleBtn.isSelected =
                        addressLayout.visibility == View.VISIBLE
            }
        }
    }
}
