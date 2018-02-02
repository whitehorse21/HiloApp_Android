package com.hiloipa.app.hilo.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
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



    companion object {
        fun newInstance(): EditContactFragment {
            val args = Bundle()
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
        // set main buttons click listener
        contactInfoToggleBtn.setOnClickListener(this)
        personalInfoToggleBtn.setOnClickListener(this)
        rodanFieldsToggleBtn.setOnClickListener(this)
        tagsAndFieldsToggleBtn.setOnClickListener(this)
        socialAndWebsitesToggleBtn.setOnClickListener(this)
        addressToggleBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.contactInfoToggleBtn -> contactInfoLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            R.id.personalInfoToggleBtn -> personalInfoLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            R.id.rodanFieldsToggleBtn -> rodanFieldsLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            R.id.tagsAndFieldsToggleBtn -> tagsAndFieldsLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            R.id.socialAndWebsitesToggleBtn -> socialAndWebsitesLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            R.id.addressToggleBtn -> addressLayout.visibility =
                    if (contactInfoLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}
