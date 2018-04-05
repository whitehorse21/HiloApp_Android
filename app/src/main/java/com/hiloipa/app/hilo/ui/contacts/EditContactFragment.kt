package com.hiloipa.app.hilo.ui.contacts


import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TagsSpinnerAdapter
import com.hiloipa.app.hilo.models.Tag
import com.hiloipa.app.hilo.models.requests.SaveContactRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.CustomField
import com.hiloipa.app.hilo.models.responses.FullContactDetails
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.NewContactData
import com.hiloipa.app.hilo.ui.contacts.details.ContactDetailsActivity
import com.hiloipa.app.hilo.ui.widget.CustomFieldView
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.utils.*
import io.reactivex.Observable
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
    var newCustomFields: MutableList<CustomFieldView> = mutableListOf()
    var newTags: MutableList<Tag> = mutableListOf()
    var giftsGiven: MutableList<CustomFieldView> = mutableListOf()
    lateinit var newContactData: NewContactData
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit_contact, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNewContactData()
        // set main buttons click listener
        contactInfoToggleBtn.setOnClickListener(this)
        personalInfoToggleBtn.setOnClickListener(this)
        rodanFieldsToggleBtn.setOnClickListener(this)
        tagsAndFieldsToggleBtn.setOnClickListener(this)
        socialAndWebsitesToggleBtn.setOnClickListener(this)
        addressToggleBtn.setOnClickListener(this)
        // save button setup
        saveBtn.setOnClickListener { saveContactData() }

        // phones typeface
        val typeface = Typeface.createFromAsset(context!!.assets, "Font/Raleway-Regular.ttf")
        phoneNumberField.typeface = typeface
        homePhoneField.typeface = typeface
        cellPhoneField.typeface = typeface
        workPhoneField.typeface = typeface
    }

    private fun getFullContactDetails(contactId: String, loading: AlertDialog = activity!!.showLoading()) {
        val request = StandardRequest()
        request.contactId = contactId
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
                        activity!!.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun getNewContactData() {
        val loading = activity!!.showLoading()
        HiloApp.api().getDataForNewContact(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<NewContactData> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.newContactData = data
                            this.setupSpinnersAndButtons(loading)
                        }
                    } else {
                        loading.dismiss()
                        activity!!.showExplanation(message = response.message)
                    }
                }, { error ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun setupSpinnersAndButtons(loading: AlertDialog) {
        // contact info
        val titles = mutableListOf<String>()
        var isTitleFromUser = false
        this.newContactData.titles.forEach { titles.add(it.text) }
        titleSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, titles)
        titleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTitleFromUser) {
                    val title = newContactData.titles[position]
                    titleField.text = title.text
                    titleField.tag = title.value
                    isTitleFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        titleField.setOnClickListener {
            isTitleFromUser = true
            titleSpinner.performClick()
        }

        // personal info
        // group
        val groups = mutableListOf<String>()
        var isGroupFromUser = false
        this.newContactData.groupsList.forEach { groups.add(it.text) }
        groupSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, groups)
        groupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isGroupFromUser) {
                    val group = newContactData.groupsList[position]
                    groupButton.text = group.text
                    groupButton.tag = group.value
                    isGroupFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        groupButton.setOnClickListener {
            isGroupFromUser = true
            groupSpinner.performClick()
        }
        // contact type
        val types = mutableListOf<String>()
        var isTypeFromUser = false
        this.newContactData.contactTypes.forEach { types.add(it.text) }
        contactTypeSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, types)
        contactTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTypeFromUser) {
                    val type = newContactData.contactTypes[position]
                    contactTypeButton.text = type.text
                    contactTypeButton.tag = type.value
                    isTypeFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        contactTypeButton.setOnClickListener {
            isTypeFromUser = true
            contactTypeSpinner.performClick()
        }
        // pipeline position
        val positions = mutableListOf<String>()
        var isPipelineFromUser = false
        this.newContactData.pipelines.forEach { positions.add(it.text) }
        pipelinePositionSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, positions)
        pipelinePositionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isPipelineFromUser) {
                    val pipeline = newContactData.pipelines[position]
                    pipelinePositionButton.text = pipeline.text
                    pipelinePositionButton.tag = pipeline.value
                    isPipelineFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        pipelinePositionButton.setOnClickListener {
            isPipelineFromUser = true
            pipelinePositionSpinner.performClick()
        }
        // lead temp
        val temps = mutableListOf<String>()
        var isTempFromUser = false
        this.newContactData.temps.forEach { temps.add(it.text) }
        tempSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, temps)
        tempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTempFromUser) {
                    val temp = newContactData.temps[position]
                    tempButton.text = temp.text
                    tempButton.tag = temp.value
                    isTempFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        tempButton.setOnClickListener {
            isTempFromUser = true
            tempSpinner.performClick()
        }
        // birth date
        birthDayButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val birthDate = Date(calendar.timeInMillis)
                val birthDateFormat = SimpleDateFormat("MMM dd", Locale.ENGLISH)
                birthDayButton.text = birthDateFormat.format(birthDate)
                birthDayButton.tag = birthDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
        // three way call
        val callWays = mutableListOf<String>(getString(R.string.yes), getString(R.string.no))
        var isWayFromUser = false
        threeWayCallSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, callWays)
        threeWayCallSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isWayFromUser) {
                    val way = callWays[position]
                    threeWayCallButton.text = way
                    threeWayCallButton.tag = way
                    isWayFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        threeWayCallButton.setOnClickListener {
            isWayFromUser = true
            threeWayCallSpinner.performClick()
        }
        // derived by
        val derivedBys = mutableListOf<String>()
        var isDerivedFromUser = false
        this.newContactData.derivedBys.forEach { derivedBys.add(it.text) }
        derivedBySpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, derivedBys)
        derivedBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isDerivedFromUser) {
                    val derivedBy = newContactData.derivedBys[position]
                    derivedByButton.text = derivedBy.text
                    derivedByButton.tag = derivedBy.value
                    isDerivedFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        derivedByButton.setOnClickListener {
            isDerivedFromUser = true
            derivedBySpinner.performClick()
        }
        // source
        val sources = mutableListOf<String>()
        var isSourceFromUser = false
        this.newContactData.sources.forEach { sources.add(it.text) }
        sourceSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, sources)
        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSourceFromUser) {
                    val source = newContactData.sources[position]
                    sourceField.text = source.text
                    sourceField.tag = source.value
                    isSourceFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        sourceField.setOnClickListener {
            isSourceFromUser = true
            sourceSpinner.performClick()
        }
        // tags and custom fields
        val tags = arrayListOf<Tag>()
        this.newContactData.allTags.split(",").forEach { tags.add(Tag(it)) }
        val adapter = TagsSpinnerAdapter(tags)
        adapter.delegate = object : TagsSpinnerAdapter.TagSpinnerDelegate {
            override fun didClickOnTag(tag: Tag, position: Int) {
                var currentText = tagsButton.text.toString()
                if (currentText.isNotEmpty() && tag.isSelected)
                    currentText = "$currentText,${tag.name}"
                else if (tag.isSelected)
                    currentText = tag.name
                else {
                    val regex = "(,)*(${tag.name})(,$)*".toRegex()
                    currentText = regex.replace(currentText, "")
                    if (currentText.startsWith(",")) currentText = "^,".toRegex()
                            .replace(currentText, "")
                }

                tagsButton.text = currentText
            }
        }
        tagsSpinner.adapter = adapter
        tagsButton.setOnClickListener { tagsSpinner.performClick() }
        addTagBtn.setOnClickListener {
            if (newTagField.text.isNotEmpty()) {
                val tag = Tag(newTagField.text.toString(), true)
                adapter.tags.add(tag)
                var currentText = tagsButton.text
                currentText = if (currentText.isNotEmpty())
                    "$currentText,${tag.name}"
                else tag.name
                tagsButton.text = currentText
                newTags.add(tag)
                newTagField.setText("")
            }
        }
        addFieldBtn.setOnClickListener {
            val fieldHint = newCustomField.text.toString()
            if (fieldHint.isNotEmpty()) {
                val customField = CustomField(fieldHint, "")
                val customFieldView = CustomFieldView(activity!!, customField)
                customFieldView.deleteClickListener(onDeleteNewFieldListener)
                this.newCustomFields.add(customFieldView)
                customFieldsLayout.addView(customFieldView)
                newCustomField.setText("")
                activity!!.hideKeyboard()
            }
        }
        // rodan plus fields
        addGiftBtn.setOnClickListener {
            val gift = giftGivenField.text.toString()
            if (gift.isNotEmpty()) {
                val giftField = CustomField(getString(R.string.gift_given), gift)
                val customFieldView = CustomFieldView(activity!!, giftField)
                customFieldView.deleteClickListener(onDeleteGiftListener)
                this.giftsGiven.add(customFieldView)
                giftFieldsContainer.addView(customFieldView)
                giftGivenField.setText("")
            }
        }
        autoShipDayButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val autoShipDate = Date(calendar.timeInMillis)
                autoShipDayButton.text = dateFormat.format(autoShipDate)
                autoShipDayButton.tag = autoShipDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
        // address
        //states
        val states = mutableListOf<String>()
        var isStateFromUser = false
        this.newContactData.states.forEach { states.add(it.text) }
        statesSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, states)
        statesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isStateFromUser) {
                    val state = newContactData.states[position]
                    stateField.text = state.text
                    stateField.tag = state.value
                    isStateFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        stateField.setOnClickListener {
            isStateFromUser = true
            statesSpinner.performClick()
        }
        // countries
        val countries = mutableListOf<String>()
        var isCountryFromUser = false
        this.newContactData.countries.forEach { countries.add(it.text) }
        countriesSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, countries)
        countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isCountryFromUser) {
                    val country = newContactData.countries[position]
                    countryField.text = country.text
                    countryField.tag = country.value
                    isCountryFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        countryField.setOnClickListener {
            isCountryFromUser = true
            countriesSpinner.performClick()
        }

        // get details from arguments if this is the case
        if (arguments!!.containsKey("contactId")) {
            val contactId = arguments!!.getString("contactId")
            getFullContactDetails(contactId, loading)
        } else loading.dismiss()
    }

    private fun updateUIWithNewDetails(contactDetails: FullContactDetails) {
        val details = contactDetails.contactDetails
        // contact info
        titleField.text = details.title
        firstNameField.setText(details.firstName)
        lastNameField.setText(details.lastName)
        emailField.setText(details.email)
        val emails = if (details.alternativeEmail != null) details.alternativeEmail
                .split(",") else arrayListOf()
        val emailFileds = mutableListOf(emailField1, emailField2, emailField3)
        if (emails.isNotEmpty()) {
            for (i in 0..emails.lastIndex) {
                val email = emails[i]
                if (email.isNotEmpty() && !email.equals("empty", true)) {
                    emailFileds[i].setText(email)
                }
            }
        }
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
        // group
        groupButton.text = details.groups
        groupButton.tag = details.groups
        // contact type
        contactTypeButton.text = details.contactType
        contactTypeButton.tag = details.contactTypePosition
        // pipeline position
        pipelinePositionButton.text = details.pipelinePosition
        pipelinePositionButton.tag = "${details.pipeline}"
        // lead temp
        tempButton.text = details.temp
        tempButton.tag = "${details.tempId}"
        // children and spouse
        childrenField.setText(details.children)
        spouseField.setText(details.spouse)
        // setup birth date button
        if (details.birthdayMonth != null && details.birthdayMonth.isNotEmpty()) {
            val calendar = Calendar.getInstance()
            try {
                val month = SimpleDateFormat("MMM", Locale.ENGLISH).parse(details.birthdayMonth)
                calendar.time = month
                calendar.set(Calendar.DAY_OF_MONTH, details.birthdayDay)
                // set seleceted date in the button if it is not null
                val date = Date(calendar.timeInMillis)
                birthDayButton.text = SimpleDateFormat("MMM dd", Locale.ENGLISH)
                        .format(date)
                birthDayButton.tag = date
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // three way to call
        threeWayCallButton.text = details.thereWayToCall
        threeWayCallButton.tag = details.thereWayToCall
        // derived by
        derivedByButton.text = details.derivedBy
        if (details.derivedBy != null)
            derivedByButton.tag = newContactData.derivedBys
                    .firstOrNull { it.text == details.derivedBy }?.value
        companyField.setText(details.company)
        // contact source
        sourceField.text = details.contactSource
        if (details.contactSource.isNotEmpty())
            sourceField.tag = newContactData.sources
                    .firstOrNull { it.text == details.contactSource }?.value
        jobTitleField.setText(details.jobTitle)

        // rodan + fields
        if (HiloApp.userData.userCompany.equals("rodan + fields", true)) {
            rodanFieldsRootLayout.visibility = View.VISIBLE
            giftYesBtn.isChecked = details.gift.equals("yes", true)
            giftNoBtn.isChecked = details.gift.equals("no", true)
            // check if we have any added gifts
            val giftsGiven = details.giftGiven
            if (giftsGiven != null) {
                // split gifts by coma into a list
                val gifts = giftsGiven.split(",")
                gifts.forEach {
                    // add a field for each gift
                    if (it.isNotEmpty()) {
                        val giftField = CustomField(getString(R.string.gift_given), it)
                        val customFieldView = CustomFieldView(activity!!, giftField)
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
            val autoShipDate = details.shipDateTime
            if (autoShipDate != null) {
                autoShipDayButton.text = dateFormat.format(autoShipDate)
            }
            // setup glow sample buttons
            enableGlowSampleBtn.isChecked = details.miniFacial.equals("yes", true)
            disableGlowSampleBtn.isChecked = details.miniFacial.equals("no", true)
        } else rodanFieldsRootLayout.visibility = View.GONE

        // tags and custom fields
        details.assignCustomFields.forEach { field ->
            val fieldInput = CustomFieldView(activity!!, field)
            fieldInput.deleteClickListener(onDeleteFieldListener)
            this.customFields.add(fieldInput)
            customFieldsLayout.addView(fieldInput)
        }
        tagsButton.text = details.contactTag
        val tagsAdapter = tagsSpinner.adapter as TagsSpinnerAdapter
        if (details.contactTag != null)
            details.contactTag.split(",").forEach { tag: String ->
                tagsAdapter.tags.firstOrNull { tag == it.name }?.isSelected = true
                tagsAdapter.notifyDataSetChanged()
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
        stateField.tag = (this.newContactData.states
                .firstOrNull { it.text == details.state }?.value) ?: ""
        zipCodeField.setText(details.zipCode)
        val country = newContactData.countries.firstOrNull { it.value == details.country }
        countryField.text = country?.text
        countryField.tag = country?.value
    }

    private val onDeleteFieldListener = View.OnClickListener { v ->
        val field: CustomFieldView = customFieldsLayout.findViewWithTag(v.tag)
        customFieldsLayout.removeView(field)
        customFields.remove(field)
    }

    private val onDeleteNewFieldListener = View.OnClickListener { v ->
        val field: CustomFieldView = customFieldsLayout.findViewWithTag(v.tag)
        customFieldsLayout.removeView(field)
        newCustomFields.remove(field)
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

    private fun saveContactData() {
        // if first or last name are not filled we can't save contact
        if (firstNameField.text.isEmpty() || lastNameField.text.isEmpty()) {
            Toast.makeText(activity, getString(R.string.fill_contact_info), Toast.LENGTH_SHORT).show()
            return
        }

        val request = SaveContactRequest()
        val regex = ",$".toRegex()
        val stringBuilder = StringBuilder()
        request.contactId = if (contactDetails != null) "${contactDetails!!.contactDetails.contactId}" else "0"
        // contact info
        request.title = titleField.tag as String? ?: ""
        request.firstName = firstNameField.text.toString()
        request.lastName = lastNameField.text.toString()
        request.email = emailField.text.toString()
        // build alternative emails
        val emailFileds = mutableListOf(emailField1, emailField2, emailField3)
        emailFileds.forEach { field: RalewayEditText ->
            val text = field.text.toString()
            if (text.isNotEmpty()) {
                stringBuilder.append(text).append(",")
            }
        }
        request.alternEmail = regex.replace(stringBuilder.toString(), "")
        stringBuilder.setLength(0)
        request.phoneNumber = phoneNumberField.text.toString()
        // build alternative phones
        val phoneFields = mutableListOf(cellPhoneField, workPhoneField, homePhoneField)
        phoneFields.forEach {
            val phone = it.text.toString()
            if (phone.isNotEmpty())
                stringBuilder.append(phone).append(",")
        }
        request.alternPhones = regex.replace(stringBuilder.toString(), "")
        stringBuilder.setLength(0)

        // personal info
        request.groups = groupButton.tag as String? ?: ""
        request.contactType = contactTypeButton.text.toString()
        request.pipelinePosition = pipelinePositionButton.text.toString()
        request.pipeline = pipelinePositionButton.tag as String? ?: ""
        request.temp = tempButton.tag as String? ?: ""
        request.children = childrenField.text.toString()
        request.spouse = spouseField.text.toString()
        val birthDate = birthDayButton.tag as Date?
        if (birthDate != null) {
            val birthDayMonthFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            request.birthDateMonth = birthDayMonthFormat.format(birthDate)
            request.birthDateDay = "${birthDate.date}"
        }
        request.thereWayCall = if ((threeWayCallButton.tag as String? ?: "no")
                        .equals("yes", true)) "true" else "false"
        request.derivedBy = derivedByButton.tag as String? ?: ""
        request.company = companyField.text.toString()
        request.source = sourceField.tag as String? ?: ""
        request.addJob = jobTitleField.text.toString()

        // rodan plus fields
        request.glowSample = "${enableGlowSampleBtn.isChecked}"
        if ((autoShipDayButton.tag as Date?) != null) {
            val date = autoShipDayButton.tag as Date
            request.shipDateTime = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date)
        }
        request.wrinkless = "${wrinklesCheckBox.isChecked}"
        request.sensit = "${sensitivityCheckBox.isChecked}"
        request.darkMark = "${darkMarksCheckBox.isChecked}"
        request.lashBrows = "${lashesBrowsCheckBox.isChecked}"
        request.sogg = "${saggingCheckBox.isChecked}"
        request.acne = "${acneCheckBox.isChecked}"
        request.dullness = "${dullnessCheckBox.isChecked}"
        request.gift = "${giftYesBtn.isChecked}"
        // build gifts string
        for (index in 0..(giftFieldsContainer.childCount - 1)) {
            val view = giftFieldsContainer.getChildAt(index) as CustomFieldView
            stringBuilder.append(view.customField.fieldValue).append(",")
        }
        request.giftGiven = regex.replace(stringBuilder.toString(), "")
        stringBuilder.setLength(0)

        // tags and custom fields
        val allTags = tagsButton.text.toString()
        this.newTags.forEach { stringBuilder.append(it.name).append(",") }
        request.assignedTags = allTags
        request.newTag = regex.replace(stringBuilder.toString(), "")
        stringBuilder.setLength(0)
        // set existent fields
        stringBuilder.append("<xml>")
        this.customFields.forEach { field: CustomFieldView ->
            stringBuilder.append("<").append(field.hint()).append(">").append(field.text())
                    .append("</").append(field.hint()).append(">")
        }
        stringBuilder.append("</xml>")
        request.customValue = stringBuilder.toString()
        stringBuilder.setLength(0)
        // set new fields
        stringBuilder.append("<xml>")
        this.newCustomFields.forEach { field: CustomFieldView ->
            stringBuilder.append("<").append(field.hint()).append(">").append(field.text())
                    .append("</").append(field.hint()).append(">")
        }
        stringBuilder.append("</xml>")
        request.newCustomFields = stringBuilder.toString()
        stringBuilder.setLength(0)

        // social and websites
        request.facebookPersonal = facebookPersonalField.text.toString()
        request.facebookBusiness = facebookBusinessField.text.toString()
        request.twitter = twitterField.text.toString()
        request.instagram = instagramField.text.toString()
        request.pinterest = pinterestField.text.toString()
        request.periscope = periscopeField.text.toString()
        request.linkedIn = linkedInField.text.toString()
        request.snapChat = snapChatField.text.toString()
        request.vine = vineField.text.toString()
        request.tumblr = tumblrField.text.toString()
        request.youTube = youTubeField.text.toString()
        request.googlePlus = googlePlusField.text.toString()
        request.meetUp = meetupField.text.toString()
        request.personalWebsite = websitePersonalField.text.toString()
        request.businessWebsite = websiteBusinessField.text.toString()
        request.other1 = other1Field.text.toString()
        request.other2 = other2Field.text.toString()

        // address
        request.street = streetField.text.toString()
        request.street2 = street2Field.text.toString()
        request.city = cityField.text.toString()
        request.countryId = countryField.tag as String? ?: ""
        request.country = countryField.text.toString()
        request.zipCode = zipCodeField.text.toString()
        request.state = stateField.text.toString()
        request.stateId = stateField.tag as String? ?: "1"
        if (request.stateId.isEmpty()) {
            request.stateId = "1"
            request.state = newContactData.states[0].text
        }

        val loading = activity!!.showLoading()
        val observable: Observable<HiloResponse<String>>
        if (contactDetails == null)
            observable = HiloApp.api().addNewContact(request)
        else
            observable = HiloApp.api().updateContactDetails(request)

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        activity!!.hideKeyboard()
                        if (data != null)
                            Toast.makeText(activity, data, Toast.LENGTH_SHORT).show()

                        activity!!.setResult(Activity.RESULT_OK)
                        if (activity is ContactDetailsActivity) {
                            val contactId = arguments!!.getString("contactId")
                            getFullContactDetails(loading = loading, contactId = contactId)
                        } else {
                            loading.dismiss()
                            activity!!.finish()
                        }
                    } else {
                        loading.dismiss()
                        activity!!.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }
}
