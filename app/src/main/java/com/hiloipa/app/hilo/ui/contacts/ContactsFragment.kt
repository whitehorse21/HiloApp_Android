package com.hiloipa.app.hilo.ui.contacts


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactsAdapter
import com.hiloipa.app.hilo.adapter.TextMessageScriptAdapter
import com.hiloipa.app.hilo.models.requests.*
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.MainActivity
import com.hiloipa.app.hilo.ui.base.BaseFragment
import com.hiloipa.app.hilo.ui.contacts.details.ContactDetailsActivity
import com.hiloipa.app.hilo.ui.contacts.details.ContactsFilterFragment
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : BaseFragment(), ContactsDelegate, TextWatcher {

    lateinit var adapter: ContactsAdapter
    lateinit var mainActivity: MainActivity
    var filterRequest: FilterRequest? = null
    var query: String = ""
    var page: Int = 1
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_contacts, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val filter = IntentFilter(ContactsFilterFragment.actionFilterContacts)
        filter.addAction(ContactsFilterFragment.actionResetFilter)
        filter.addAction(ImportDialogFragment.actionContactsImported)
        LocalBroadcastManager.getInstance(mainActivity).registerReceiver(broadcastReceiver, filter)

        adapter = ContactsAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(activity)
        searchField.addTextChangedListener(this)

        mainActivity.importContactsBtn.visibility = View.VISIBLE
        mainActivity.importContactsBtn.setOnClickListener { importContactsFromDevice() }

        addContactBtn.setOnClickListener {
            val intent = Intent(activity, EditContactActivity::class.java)
            activity!!.startActivityForResult(intent, 1250)
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

        adapter.contacts.clear()
        this.loadContactsFromServer()
    }

    private fun loadContactsFromServer(page: Int = this.page, query: String = this.query) {
        val request = ContactsListRequest()
        request.page = page
        request.query = query

        val observable: Observable<HiloResponse<DetailedContacs>>
        if (filterRequest != null && query.isEmpty())
            observable = HiloApp.api().filterContacts(filterRequest!!)
        else if (query.isEmpty())
            observable = HiloApp.api().getContactsList(request)
        else
            observable = HiloApp.api().searchDetailedContacts(request)

        val loading = activity!!.showLoading()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapSingle {response: HiloResponse<DetailedContacs> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            if (page == data.totalPages)
                                loadMoreBtn.visibility = View.GONE
                            getDetaiList(data)
                        } else{
                            Single.error(Throwable("Received data is not valid. "))
                        }
                    } else {
                        Single.error(Throwable(response.message))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: List<ContactWrapper> ->
                    loading.dismiss()
                            adapter.addContacts(response)
                            // hide load more button if we've reached the maximum pages

                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun getDetaiList(data: DetailedContacs): Single<List<ContactWrapper>> {
        return Observable.fromIterable(data.contacts)
                .subscribeOn(Schedulers.io())
                .flatMap {detailedContact ->
                    val request = StandardRequest()
                    request.contactId = detailedContact.id.toString()
                    HiloApp.api()
                            .getContactFullDetails(request)
                            .map { ContactWrapper(detailedContact, it.data!!.contactDetails) }
                }.toList()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when (intent.action) {
                ContactsFilterFragment.actionFilterContacts -> {
                    val filterRequest: FilterRequest = intent.extras.getParcelable("filter")
                    this@ContactsFragment.filterRequest = filterRequest
                    adapter.contacts.clear()
                    page = 1
                    query = ""
                    loadContactsFromServer()
                }

                ImportDialogFragment.actionContactsImported,
                ContactsFilterFragment.actionResetFilter -> {
                    this@ContactsFragment.filterRequest = null
                    adapter.contacts.clear()
                    page = 1
                    query = ""
                    loadContactsFromServer()
                }
            }
        }
    }

    override fun onContactClicked(contact: DetailedContact, position: Int) {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        val extras = Bundle()
        extras.putString(ContactDetailsActivity.contactIdKey, "${contact.id}")
        intent.putExtras(extras)
        activity!!.startActivity(intent)
    }

    override fun onEditContactClicked(contact: DetailedContact, position: Int) {
        val intent = Intent(activity, EditContactActivity::class.java)
        intent.putExtra(EditContactActivity.contactIdKey, "${contact.id}")
        activity!!.startActivityForResult(intent, 1250)
    }

    override fun onDeleteContactClicked(contact: DetailedContact, position: Int) {
        val dialog = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    deleteContact("${contact.id}")
                })
                .setNegativeButton(getString(R.string.no), { dialog, which ->
                    dialog.dismiss()
                })
                .create()

        dialog.show()
    }

    private fun deleteContact(contactId: String) {
        val loading = activity!!.showLoading()
        val request = DeleteContactRequest()
        request.id = contactId
        HiloApp.api().deleteContact(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val contactsId = contactId.split(",")
                        val contacts = mutableListOf<DetailedContact>()
                        contactsId.forEach { id: String ->
                            contacts.add(adapter.contacts
                                    .first { it.id == id.toInt() })
                        }
                        contacts.forEach {
                            val index = adapter.contacts.indexOf(it)
                            adapter.contacts.removeAt(index)
                            adapter.notifyItemRemoved(index)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onSendSmsClicked(contact: DetailedContact, position: Int) {
        val request = StandardRequest()
        request.contactId = contact.id.toString()
        val loading = activity!!.showLoading()
        HiloApp.api().getContactFullDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FullContactDetails> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            getScripts(data.contactDetails, loading)
                        else loading.dismiss()
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun getScripts(contact: ContactDetails, loading: AlertDialog = activity!!.showLoading()) {
        HiloApp.api().getScripts(GetSmsScriptsRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ArrayList<MessageScript>> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            sendMessageToContact(contact, data)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun sendMessageToContact(contact: ContactDetails, scripts: ArrayList<MessageScript>) {
        val dialogView = layoutInflater.inflate(R.layout.alert_send_message, null)
        val sendBtn: RalewayButton = dialogView.findViewById(R.id.sendBtn)
        val messageField: RalewayEditText = dialogView.findViewById(R.id.messageInput)
        val contactPhoneLabel: RalewayTextView = dialogView.findViewById(R.id.phoneNumberLabel)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.recyclerView)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)

        val adapter = TextMessageScriptAdapter(scripts)
        adapter.delegate = object : TextMessageScriptAdapter.ScriptDelegate {
            override fun onInsertTextClicked(script: MessageScript, position: Int) {
                messageField.setText(script.body)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var phoneNumber = ""
        if (contact.contactNumber != null && contact.contactNumber.isNotEmpty()) {
            contactPhoneLabel.text = getString(R.string.phone_number_s, contact.contactNumber)
            phoneNumber = contact.contactNumber
        } else if (contact.alternatephns != null && contact.alternatephns.isNotEmpty()) {
            val alternPhones = contact.alternatephns.split(",")
            if (alternPhones.isNotEmpty() && !alternPhones[0].equals("empty", true)) {
                contactPhoneLabel.text = getString(R.string.phone_number_s, alternPhones[0])
                phoneNumber = alternPhones[0]
            }
        }

        val dialog = AlertDialog.Builder(activity!!)
                .setView(dialogView)
                .create()

        backBtn.setOnClickListener {
            dialog.dismiss()
            activity!!.hideKeyboard()
        }

        sendBtn.setOnClickListener {
            val sendMessageIntent = Intent(Intent.ACTION_VIEW)
            sendMessageIntent.data = Uri.parse("sms:${phoneNumber}")
            sendMessageIntent.putExtra("sms_body", messageField.text.toString())
            activity!!.startActivity(sendMessageIntent)
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showBulkActions() {
        val selectedContacts = adapter.contacts.filter { it.isSelected }
        // check if user has selected at least one contact from the list
        if (selectedContacts.isEmpty()) {
            activity!!.showExplanation(message = getString(R.string.no_selected_contacts))
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.alert_contact_actions, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)
        val addToGoalTrackerBtn: RalewayButton = dialogView.findViewById(R.id.addToGoalTrackerBtn)
        val bulkUpdateBtn: RalewayButton = dialogView.findViewById(R.id.bulkUpdateBtn)
        val assignCampaignBtn: RalewayButton = dialogView.findViewById(R.id.assignCampaignBtn)
        val deleteBtn: RalewayButton = dialogView.findViewById(R.id.deleteBtn)
        val selectedCountLabel: RalewayTextView = dialogView.findViewById(R.id.contactsSelectedLabel)

        selectedCountLabel.text = getString(R.string.d_contacts_selected, selectedContacts.size)

        val contatctsBuilder = StringBuilder()
        selectedContacts.forEach { contatctsBuilder.append(it.id).append(",") }
        val regex = "(,$)*".toRegex()
        val contacts = regex.replace(contatctsBuilder.toString(), "")

        val dialog = AlertDialog.Builder(activity!!).setView(dialogView).create()

        deleteBtn.setOnClickListener {
            dialog.dismiss()
            deleteContact(contacts)
        }

        bulkUpdateBtn.setOnClickListener { showUpdateContactDialog(selectedContacts, dialog) }
        assignCampaignBtn.setOnClickListener { showEmailCampaignsDialog() }
        addToGoalTrackerBtn.setOnClickListener {
            dialog.dismiss()
            val loading = activity!!.showLoading()
            val request = AddToTrackerRequest()
            request.list = contacts
            HiloApp.api().addToGoalTracker(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.status.isSuccess()) {
                            activity!!.showExplanation(getString(R.string.success),
                                    getString(R.string.contacts_added_to_tracker))
                        } else activity!!.showExplanation(message = response.message)
                    }, { error: Throwable ->
                        loading.dismiss()
                        activity!!.showExplanation(message = error.localizedMessage)
                        error.printStackTrace()
                    })
        }

        backBtn.setOnClickListener {
            activity!!.hideKeyboard()
            dialog.dismiss()
        }
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showUpdateContactDialog(selectedContacts: List<DetailedContact>, parentDialog: AlertDialog) {
        val dialogView = layoutInflater.inflate(R.layout.alert_bulk_update, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)
        val fieldToEditBtn: RalewayButton = dialogView.findViewById(R.id.fieldToEditBtn)
        val fieldToEditSpinner: Spinner = dialogView.findViewById(R.id.fieldToUpdateSpinner)
        val newValueFieldLayout: LinearLayout = dialogView.findViewById(R.id.newValueFieldLayout)
        val newValueSpinnerLayout: LinearLayout = dialogView.findViewById(R.id.newValueSpinnerLayout)
        val newValueDateLayout: LinearLayout = dialogView.findViewById(R.id.newValueDateLayout)
        val newValueCheckBoxesLayout: LinearLayout = dialogView.findViewById(R.id.newValueCheckBoxesLayout)
        val newValueRadioLayout: RadioGroup = dialogView.findViewById(R.id.newValueRadioLayout)
        val layouts = mutableListOf<View>(newValueFieldLayout, newValueSpinnerLayout, newValueDateLayout,
                newValueCheckBoxesLayout, newValueRadioLayout)
        val updateBtn: RalewayButton = dialogView.findViewById(R.id.updateBtn)

        val loading = activity!!.showLoading()
        HiloApp.api().getBulkUpdateValues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<BulkUpdate> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            val valuesToEdit = mutableListOf<String>()
                            data.fieldsAvailable.forEach { valuesToEdit.add(it.text) }
                            fieldToEditSpinner.adapter = ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_dropdown_item, valuesToEdit)
                            fieldToEditSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    val valueToEdit = data.fieldsAvailable[position]
                                    activity!!.hideKeyboard()
                                    when (valueToEdit.value) {
                                        "3-way", "Gift", "Give It A Glow Sample" -> {
                                            newValueRadioLayout.visibility = View.VISIBLE
                                            newValueRadioLayout.clearCheck()
                                            layouts.forEach {
                                                if (it != newValueRadioLayout)
                                                    it.visibility = View.GONE
                                            }
                                        }
                                        "Auto Ship Date" -> {
                                            newValueDateLayout.visibility = View.VISIBLE
                                            val dateBtn: RalewayButton = newValueDateLayout
                                                    .findViewById(R.id.autoShipDayButton)
                                            dateBtn.setOnClickListener {
                                                val calendar = Calendar.getInstance()
                                                val datePicker = DatePickerDialog(activity,
                                                        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                                            calendar.set(year, month, dayOfMonth)
                                                            val autoShipDate = Date(calendar.timeInMillis)
                                                            dateBtn.text = dateFormat.format(autoShipDate)
                                                            newValueDateLayout.tag = autoShipDate
                                                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                                        calendar.get(Calendar.DAY_OF_MONTH))
                                                datePicker.show()
                                            }
                                            layouts.forEach {
                                                if (it != newValueDateLayout)
                                                    it.visibility = View.GONE
                                            }
                                        }
                                        "Concerns" -> {
                                            newValueCheckBoxesLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueCheckBoxesLayout)
                                                    it.visibility = View.GONE
                                            }
                                        }
                                        "Contact Type" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val types = mutableListOf<String>()
                                            data.contactTypes.forEach { types.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    types)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.contactTypes[position]
                                                    newValueSpinnerLayout.tag = type.value
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        "Derived By" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val derivedBys = mutableListOf<String>()
                                            data.derivedBys.forEach { derivedBys.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    derivedBys)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.derivedBys[position]
                                                    newValueSpinnerLayout.tag = type.value
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        "Group" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val groups = mutableListOf<String>()
                                            data.groups.forEach { groups.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    groups)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.groups[position]
                                                    newValueSpinnerLayout.tag = type.value
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        "Temp" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val temps = mutableListOf<String>()
                                            data.leadTemps.forEach { temps.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    temps)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.leadTemps[position]
                                                    newValueSpinnerLayout.tag = "${type.value}"
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        "Pipeline Position" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val positions = mutableListOf<String>()
                                            data.pipelinePositions.forEach { positions.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    positions)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.pipelinePositions[position]
                                                    newValueSpinnerLayout.tag = type.value
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        "Remove Tag" -> {
                                            newValueSpinnerLayout.visibility = View.VISIBLE
                                            layouts.forEach {
                                                if (it != newValueSpinnerLayout)
                                                    it.visibility = View.GONE
                                            }
                                            val spinner: Spinner = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeSpinner)
                                            val btn: RalewayButton = newValueSpinnerLayout
                                                    .findViewById(R.id.valueToChangeBtn)
                                            val tags = mutableListOf<String>()
                                            data.tagsToRemove.forEach { tags.add(it.text) }
                                            spinner.adapter = ArrayAdapter<String>(activity,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    tags)
                                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                    val type = data.tagsToRemove[position]
                                                    newValueSpinnerLayout.tag = type.value
                                                    btn.text = type.text
                                                }

                                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                            }
                                            btn.setOnClickListener { spinner.performClick() }
                                        }
                                        else -> {
                                            newValueFieldLayout.visibility = View.VISIBLE
                                            val field: RalewayEditText = newValueFieldLayout
                                                    .findViewById(R.id.newValueField)
                                            field.setText("")
                                            layouts.forEach {
                                                if (it != newValueFieldLayout)
                                                    it.visibility = View.GONE
                                            }
                                        }
                                    }
                                    fieldToEditBtn.text = valueToEdit.text
                                    fieldToEditBtn.tag = valueToEdit.value
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                            fieldToEditBtn.setOnClickListener { fieldToEditSpinner.performClick() }

                            val dialog = AlertDialog.Builder(activity!!).setView(dialogView).create()

                            backBtn.setOnClickListener {
                                activity!!.hideKeyboard()
                                dialog.dismiss()
                            }

                            updateBtn.setOnClickListener {
                                val valueToChange = fieldToEditBtn.tag as String?
                                var newValue: String? = null
                                if (valueToChange != null) {
                                    when (valueToChange) {
                                        "3-way", "Gift", "Give It A Glow Sample" ->
                                            newValue = "${newValueRadioLayout.checkedRadioButtonId == R.id.bulkPositiveBtn}"
                                        "Contact Type", "Concerns", "Derived By", "Group", "Temp",
                                        "Pipeline Position", "Remove Tag" ->
                                                newValue = newValueSpinnerLayout.tag as String
                                        "Auto Ship Date" ->
                                                newValue = newValueDateLayout.tag as String
                                        else -> {
                                            val field: RalewayEditText = newValueFieldLayout.findViewById(R.id.newValueField)
                                            newValue = field.text.toString()
                                        }
                                    }
                                }
                                val regex = "(,$)*".toRegex()
                                val contactsBuilder = StringBuilder()
                                selectedContacts.forEach {
                                    contactsBuilder.append(it.id.toString()).append(",")
                                }
                                val contacts = regex.replace(contactsBuilder.toString(), "")
                                val request = BulkUpdateRequest()
                                if (valueToChange != null && newValue != null) {
                                    request.contacts = contacts
                                    request.selectedValue = newValue
                                    request.selectedOption = valueToChange

                                    val bulkLoading = activity!!.showLoading()
                                    HiloApp.api().bulkUpdateContact(request)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({ response: HiloResponse<String> ->
                                                bulkLoading.dismiss()
                                                if (response.status.isSuccess()) {
                                                    dialog.dismiss()
                                                    Toast.makeText(activity,
                                                            getString(R.string.contacts_update_successfully),
                                                            Toast.LENGTH_SHORT).show()
                                                    parentDialog.dismiss()
                                                    adapter.contacts.clear()
                                                    page = 1
                                                    loadContactsFromServer()
                                                } else activity!!.showExplanation(message = response.message)
                                            }, { error: Throwable ->
                                                bulkLoading.dismiss()
                                                error.printStackTrace()
                                                activity!!.showExplanation(message = error.localizedMessage)
                                            })
                                }
                            }

                            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun showEmailCampaignsDialog() {
        val selectedContacts = adapter.contacts.filter { it.isSelected }
        val dialogView = layoutInflater.inflate(R.layout.alert_assign_campaign, null)
        val campaignsBtn: RalewayButton = dialogView.findViewById(R.id.emailCampaignsBtn)
        val spinner: Spinner = dialogView.findViewById(R.id.emailCampaignsSpinner)
        val assignBtn: RalewayButton = dialogView.findViewById(R.id.updateBtn)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)

        val loading = activity!!.showLoading()
        HiloApp.api().getCampaignData(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<CampaignsData> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            val campaigns = mutableListOf<String>()
                            data.campaigns.forEach { campaigns.add(it.name) }
                            spinner.adapter = ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_dropdown_item, campaigns)
                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    val campaign = data.campaigns[position]
                                    campaignsBtn.text = campaign.name
                                    campaignsBtn.tag = campaign.id
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                            campaignsBtn.setOnClickListener { spinner.performClick() }

                            val dialog = AlertDialog.Builder(activity!!).setView(dialogView).create()

                            assignBtn.setOnClickListener {
                                val contatctsBuilder = StringBuilder()
                                selectedContacts.forEach { contatctsBuilder.append(it.id).append(",") }
                                val regex = "(,$)*".toRegex()
                                val contacts = regex.replace(contatctsBuilder.toString(), "")

                                val assignLoading = activity!!.showLoading()
                                val request = AssignCampaignRequest()
                                request.contactId = contacts
                                request.campaignId = "${campaignsBtn.tag as Int}"
                                HiloApp.api().assignCampaign(request)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ response: HiloResponse<String> ->
                                            assignLoading.dismiss()
                                            if (response.status.isSuccess()) {
                                                dialog.dismiss()
                                                activity!!.showExplanation(title = getString(R.string.success),
                                                        message = response.message)
                                            } else activity!!.showExplanation(message = response.message)
                                        }, { error: Throwable ->
                                            assignLoading.dismiss()
                                            error.printStackTrace()
                                            activity!!.showExplanation(message = error.localizedMessage)
                                        })
                            }

                            backBtn.setOnClickListener { dialog.dismiss() }
                            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            dialog.show()
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
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
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_CONTACTS), 1200)
        } else {
            val loading = activity!!.showLoading()
            mainActivity.getDeviceContacts().subscribeOn(Schedulers.io())
                    .subscribe { contacts: ArrayList<DeviceContact> ->
                        loading.dismiss()
                        val dialog = ImportDialogFragment.newInstance(contacts)
                        dialog.show(activity!!.fragmentManager, "Import")
                    }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importContactsFromDevice()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1250 && resultCode == Activity.RESULT_OK) {
            adapter.contacts.clear()
            page = 1
            loadContactsFromServer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.importContactsBtn.visibility = View.GONE
        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(broadcastReceiver)
    }
}

class DeviceContact(@JsonProperty("ContactNumber") var ContactNumber: String): Parcelable {
    @JsonProperty("Email") lateinit var email: String
    @JsonProperty("FirstName") lateinit var firstName: String
    @JsonProperty("LastName") lateinit var lastName: String
    @JsonProperty("PhotoData") var photoData: String = ""
    @JsonProperty("UUID") var uuid: String = UUID.randomUUID().toString()
    @JsonIgnore var photoPath: Uri? = null

    constructor(parcel: Parcel) : this(parcel.readString()) {
        email = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        photoData = parcel.readString()
        uuid = parcel.readString()
        photoPath = parcel.readParcelable(Uri::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ContactNumber)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photoData)
        parcel.writeString(uuid)
        parcel.writeParcelable(photoPath, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceContact> {
        override fun createFromParcel(parcel: Parcel): DeviceContact {
            return DeviceContact(parcel)
        }

        override fun newArray(size: Int): Array<DeviceContact?> {
            return arrayOfNulls(size)
        }
    }
}
