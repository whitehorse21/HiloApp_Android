package com.hiloipa.app.hilo.ui.contacts


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.fasterxml.jackson.annotation.JsonProperty

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactsAdapter
import com.hiloipa.app.hilo.models.requests.ContactsListRequest
import com.hiloipa.app.hilo.models.requests.DeleteContactRequest
import com.hiloipa.app.hilo.models.requests.ImportContactsRequest
import com.hiloipa.app.hilo.models.responses.DetailedContacs
import com.hiloipa.app.hilo.models.responses.DetailedContact
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.ImportContactsResponse
import com.hiloipa.app.hilo.ui.MainActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contacts.*


/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment(), ContactsDelegate, TextWatcher {

    lateinit var adapter: ContactsAdapter
    lateinit var mainActivity: MainActivity
    var query: String = ""
    var page: Int = 1

    companion object {
        fun newInstance(): ContactsFragment {
            val args = Bundle()
            val fragment = ContactsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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
        searchField.addTextChangedListener(this)

        mainActivity.importContactsBtn.visibility = View.VISIBLE

        mainActivity.importContactsBtn.setOnClickListener { importContactsFromDevice() }

        addContactBtn.setOnClickListener {
            val intent = Intent(activity, EditContactActivity::class.java)
            activity.startActivity(intent)
        }

        filterBtn.setOnClickListener {
            ContactsFilterFragment.newInstance().show(childFragmentManager, "ContactsFilterFragment")
        }

        actionsBtn.setOnClickListener { showBulkActions() }

        loadMoreBtn.setOnClickListener {
            page++
            loadContactsFromServer()
        }

        goBtn.setOnClickListener {
            page = 1
            adapter.contacts.clear()
            query = searchField.text.toString()
            this.loadContactsFromServer()
        }

        this.loadContactsFromServer()
    }

    private fun loadContactsFromServer(page: Int = this.page, query: String = this.query) {
        val request = ContactsListRequest()
        request.page = page
        request.query = query

        val observable: Observable<HiloResponse<DetailedContacs>>
        if (query.isEmpty()) observable = HiloApp.api().getContactsList(request)
        else observable = HiloApp.api().searchDetailedContacts(request)

        val loading = activity.showLoading()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<DetailedContacs> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            adapter.addContacts(data.contacts)
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onContactClicked(contact: DetailedContact, position: Int) {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        val extras = Bundle()
        extras.putString(ContactDetailsActivity.contactIdKey, "${contact.id}")
        intent.putExtras(extras)
        activity.startActivity(intent)
    }

    override fun onEditContactClicked(contact: DetailedContact, position: Int) {
        val intent = Intent(activity, EditContactActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onDeleteContactClicked(contact: DetailedContact, position: Int) {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity.showLoading()
                    val request = DeleteContactRequest()
                    request.id = "${contact.id}"
                    HiloApp.api().deleteContact(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    adapter.contacts.remove(contact)
                                    adapter.notifyItemRemoved(position)
                                } else activity.showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), { dialog, which ->
                    dialog.dismiss()
                })
                .create()

        dialog.show()
    }

    override fun onSendSmsClicked(contact: DetailedContact, position: Int) {

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
        val assignBtn: RalewayButton = dialogView.findViewById(R.id.emailCampaignsBtn)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)

        val dialog = AlertDialog.Builder(activity).setView(dialogView).create()

        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val query = searchField.text.toString()
        if (query.isEmpty()) {
            adapter.contacts.clear()
            this.page = 1
            this.query = ""
            loadContactsFromServer()
        }
    }

    private fun importContactsFromDevice() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1200)
        } else {
            val loading = activity.showLoading()
            mainActivity.getDeviceContacts().subscribeOn(Schedulers.io())
                    .subscribe { contacts: ArrayList<DeviceContact> ->
                        val request = ImportContactsRequest()
                        request.contacts = contacts
                        HiloApp.api().importContacts(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({response: HiloResponse<ImportContactsResponse> ->
                                    loading.dismiss()
                                    if (response.status.isSuccess()) {
                                        val data = response.data
                                        if (data != null) {
                                            val message = "Imported: ${data.imported}\nSkipped: ${data.skipped}"
                                            activity.showExplanation(title = getString(R.string.success),
                                                    message = message)
                                            adapter.contacts.clear()
                                            page = 1
                                            query = ""
                                            loadContactsFromServer()
                                        }
                                    } else activity.showExplanation(message = response.message)
                                }, { error: Throwable ->
                                    loading.dismiss()
                                    error.printStackTrace()
                                    activity.showExplanation(message = error.localizedMessage)
                                })
                    }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.importContactsBtn.visibility = View.GONE
    }
}

class DeviceContact() {
    @JsonProperty("ContactNumber")
    lateinit var number: String
    @JsonProperty("Email")
    lateinit var email: String
    @JsonProperty("FirstName")
    lateinit var firstName: String
    @JsonProperty("LastName")
    lateinit var lastName: String
    @JsonProperty("PhotoData")
    var photoData: String = ""
}
