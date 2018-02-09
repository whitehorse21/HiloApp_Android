package com.hiloipa.app.hilo.ui.contacts


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactsAdapter
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.ContactsDelegate
import com.hiloipa.app.hilo.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_contacts.*


/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment(), ContactsDelegate {

    lateinit var adapter: ContactsAdapter

    companion object {
        fun newInstance(): ContactsFragment {
            val args = Bundle()
            val fragment = ContactsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_contacts, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(activity)

        addContactBtn.setOnClickListener {
            val intent = Intent(activity, EditContactActivity::class.java)
            activity.startActivity(intent)
        }

        filterBtn.setOnClickListener {
            ContactsFilterFragment.newInstance().show(childFragmentManager, "ContactsFilterFragment")
        }

        actionsBtn.setOnClickListener { showBulkActions() }
    }

    override fun onContactClicked() {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onEditContactClicked() {
        val intent = Intent(activity, EditContactActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onDeleteContactClicked() {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                })
                .setNegativeButton(getString(R.string.no), { dialog, which ->
                    dialog.dismiss()
                })
                .create()

        dialog.show()
    }

    override fun onSendSmsClicked() {

    }

    private fun showBulkActions() {
        val dialogView = layoutInflater.inflate(R.layout.alert_contact_actions, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)
        val addToGoalTrackerBtn: RalewayButton = dialogView.findViewById(R.id.addToGoalTrackerBtn)
        val bulkUpdateBtn: RalewayButton = dialogView.findViewById(R.id.bulkUpdateBtn)
        val assignCampaignBtn: RalewayButton = dialogView.findViewById(R.id.assignCampaignBtn)
        val deleteBtn: RalewayButton = dialogView.findViewById(R.id.deleteBtn)
        val selectedCountLabel: RalewayTextView = dialogView.findViewById(R.id.contactsSelectedLabel)

        val dialog = AlertDialog.Builder(activity).setView(dialogView).create()

        bulkUpdateBtn.setOnClickListener { showUpdateContactDialog() }
        assignCampaignBtn.setOnClickListener { showEmailCampaignsDialog() }

        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showUpdateContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_bulk_update, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)
        val fieldToEdit: RalewayEditText = dialogView.findViewById(R.id.fieldToEditField)
        val newValueField: RalewayEditText = dialogView.findViewById(R.id.newValueField)
        val updateBtn: RalewayButton = dialogView.findViewById(R.id.updateBtn)

        val dialog = AlertDialog.Builder(activity).setView(dialogView).create()

        backBtn.setOnClickListener {
            activity.hideKeyboard()
            dialog.dismiss()
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showEmailCampaignsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_assign_campaign, null)
        val campaignsBtn: RalewayButton = dialogView.findViewById(R.id.emailCampaignsBtn)
        val spinner: Spinner = dialogView.findViewById(R.id.emailCampaignsSpinner)
        val assignBtn: RalewayButton = dialogView.findViewById(R.id.assignCampaignBtn)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)

        val dialog = AlertDialog.Builder(activity).setView(dialogView).create()

        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }
}
