package com.hiloipa.app.hilo.ui.contacts


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.FullContactDetails
import kotlinx.android.synthetic.main.edit_address.*
import kotlinx.android.synthetic.main.edit_contact_info.*
import kotlinx.android.synthetic.main.edit_personal_info.*
import kotlinx.android.synthetic.main.edit_rodan_plus_fields.*
import kotlinx.android.synthetic.main.edit_social_and_websites.*
import kotlinx.android.synthetic.main.edit_tags_and_custom_fields.*


/**
 * A simple [Fragment] subclass.
 */
class EditContactFragment : Fragment(), View.OnClickListener {

    var contactDetails: FullContactDetails? = null

    companion object {
        fun newInstance(details: FullContactDetails? = null): EditContactFragment {
            val args = Bundle()
            if (details != null)
                args.putParcelable("details", details)
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
        if (arguments.containsKey("details"))
            this.contactDetails = arguments.getParcelable("details")
        // update data if we have contact details
        if (contactDetails != null)
            updateUIWithNewDetails(contactDetails!!)
        // set main buttons click listener
        contactInfoToggleBtn.setOnClickListener(this)
        personalInfoToggleBtn.setOnClickListener(this)
        rodanFieldsToggleBtn.setOnClickListener(this)
        tagsAndFieldsToggleBtn.setOnClickListener(this)
        socialAndWebsitesToggleBtn.setOnClickListener(this)
        addressToggleBtn.setOnClickListener(this)
    }

    private fun updateUIWithNewDetails(contactDetails: FullContactDetails) {
        // contact info
        val details = contactDetails.contactDetails
        titleField.text = details.title
        firstNameField.setText(details.firstName)
        lastNameField.setText(details.lastName)
        emailField.setText(details.email)
        emailField1.setText(details.alternativeEmail)
        phoneNumberField.setText(details.contactNumber)
//        cellPhoneField.setText(details.)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.contactInfoToggleBtn -> {
                contactInfoLayout.visibility =
                        if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                contactInfoToggleBtn.isSelected =
                        if (contactInfoLayout.visibility == View.VISIBLE) true else false
            }
            R.id.personalInfoToggleBtn -> {
                personalInfoLayout.visibility =
                        if (personalInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                personalInfoToggleBtn.isSelected =
                        if (personalInfoLayout.visibility == View.VISIBLE) true else false
            }
            R.id.rodanFieldsToggleBtn -> {
                rodanFieldsLayout.visibility =
                        if (rodanFieldsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                rodanFieldsToggleBtn.isSelected =
                        if (rodanFieldsLayout.visibility == View.VISIBLE) true else false
            }
            R.id.tagsAndFieldsToggleBtn -> {
                tagsAndFieldsLayout.visibility =
                        if (tagsAndFieldsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                tagsAndFieldsToggleBtn.isSelected =
                        if (tagsAndFieldsLayout.visibility == View.VISIBLE) true else false
            }
            R.id.socialAndWebsitesToggleBtn -> {
                socialAndWebsitesLayout.visibility =
                        if (socialAndWebsitesLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                socialAndWebsitesToggleBtn.isSelected =
                        if (socialAndWebsitesLayout.visibility == View.VISIBLE) true else false
            }
            R.id.addressToggleBtn -> {
                addressLayout.visibility =
                        if (addressLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                addressToggleBtn.isSelected =
                        if (addressLayout.visibility == View.VISIBLE) true else false
            }
        }
    }
}
