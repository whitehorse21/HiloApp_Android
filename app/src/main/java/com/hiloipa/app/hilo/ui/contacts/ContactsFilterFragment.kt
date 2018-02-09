package com.hiloipa.app.hilo.ui.contacts

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.alert_contact_filter.*

/**
 * Created by eduardalbu on 09.02.2018.
 */
class ContactsFilterFragment(): BottomSheetDialogFragment() {

     companion object {
         fun newInstance(): ContactsFilterFragment {
             val args = Bundle()
             val fragment = ContactsFilterFragment()
             fragment.arguments = args
             return fragment
         }
     }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.alert_contact_filter, container)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { this.dismiss() }
    }
}