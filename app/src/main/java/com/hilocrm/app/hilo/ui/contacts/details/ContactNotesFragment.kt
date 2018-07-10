/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.ui.contacts.details


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.adapter.ContactNotesAdapter
import com.hilocrm.app.hilo.models.requests.DeleteContactNote
import com.hilocrm.app.hilo.models.requests.SaveContactNote
import com.hilocrm.app.hilo.models.requests.StandardRequest
import com.hilocrm.app.hilo.models.responses.ContactNote
import com.hilocrm.app.hilo.models.responses.DisplayContactNotes
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.ui.widget.RalewayButton
import com.hilocrm.app.hilo.ui.widget.RalewayEditText
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contact_notes.*


/**
 * A simple [Fragment] subclass.
 */
class ContactNotesFragment : BaseFragment(), ContactNotesAdapter.ContactNoteDelegate {

    lateinit var adapter: ContactNotesAdapter

    companion object {
        fun newInstance(contactId: String): ContactNotesFragment {
            val args = Bundle()
            args.putString("contactId", contactId)
            val fragment = ContactNotesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contact_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactNotesAdapter(activity!!)
        adapter.delegate = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        addNoteBtn.setOnClickListener { this.showNoteDialog() }

        getContactNotes(arguments!!.getString("contactId"))
    }

    override fun onEditNoteClicked(note: ContactNote, position: Int) {
        this.showNoteDialog(note)
    }

    private fun getContactNotes(contactId: String = arguments!!.getString("contactId")) {
        val loading = activity!!.showLoading()
        val request = StandardRequest()
        request.contactId = contactId
        HiloApp.api().getContactNotes(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<DisplayContactNotes> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            adapter.refreshNotes(data.notes)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onDeleteNoteClicked(note: ContactNote, position: Int) {
        val dialog = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(getString(R.string.yes), { dialog, _ ->
                    dialog.dismiss()
                    deleteNote(note, position)
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()

        dialog.show()
    }

    private fun deleteNote(note: ContactNote, position: Int) {
        val loading = activity!!.showLoading()
        val request = DeleteContactNote()
        request.noteId = "${note.id}"
        request.contactId = arguments!!.getString("contactId")

        HiloApp.api().deleteContactNote(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        adapter.notes.remove(note)
                        adapter.notifyItemRemoved(position)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun showNoteDialog(note: ContactNote? = null) {
        val alertView = layoutInflater.inflate(R.layout.alert_edit_contact_note, null)
        val titleField: RalewayEditText = alertView.findViewById(R.id.titleField)
        val descriptionField: RalewayEditText = alertView.findViewById(R.id.descriptionField)
        val cancelBtn: RalewayButton = alertView.findViewById(R.id.cancelBtn)
        val saveBtn: RalewayButton = alertView.findViewById(R.id.saveBtn)

        val dialog = AlertDialog.Builder(activity!!)
                .setView(alertView)
                .create()

        if (note != null) {
            titleField.setText(note.title)
            descriptionField.setText(String(Base64.decode(note.content, Base64.DEFAULT)))
        }

        cancelBtn.setOnClickListener { dialog.dismiss() }
        saveBtn.setOnClickListener {
            val title = titleField.text.toString()
            val content = descriptionField.text.toString()
            if (title.isEmpty()) {
                Toast.makeText(activity, getString(R.string.enter_s, titleField.hint),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (content.isEmpty()) {
                Toast.makeText(activity, getString(R.string.enter_s, descriptionField.hint),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = SaveContactNote()
            request.content = Base64.encodeToString(content.toByteArray(), Base64.DEFAULT)
            request.title = title
            request.contactId = arguments!!.getString("contactId")
            if (note != null)
                request.noteId = "${note.id}"

            dialog.dismiss()
            val loading = activity!!.showLoading()
            HiloApp.api().saveContactNote(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.status.isSuccess()) {
                            getContactNotes()
                        } else activity!!.showExplanation(message = response.message)
                    }, { error: Throwable ->
                        loading.dismiss()
                        error.printStackTrace()
                        activity!!.showExplanation(message = error.localizedMessage)
                    })
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}
