package com.hiloipa.app.hilo.ui.contacts


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactNotesAdapter
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import kotlinx.android.synthetic.main.fragment_contact_notes.*


/**
 * A simple [Fragment] subclass.
 */
class ContactNotesFragment : Fragment(), ContactNotesAdapter.ContactNoteDelegate {

    lateinit var adapter: ContactNotesAdapter

    companion object {
        fun newInstance(): ContactNotesFragment {
            val args = Bundle()
            val fragment = ContactNotesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_contact_notes, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactNotesAdapter(activity)
        adapter.delegate = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        addNoteBtn.setOnClickListener { this.showNoteDialog() }
    }

    override fun onEditNoteClicked() {
        this.showNoteDialog()
    }

    override fun onDeleteNoteClicked() {
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

    private fun showNoteDialog() {
        val alertView = layoutInflater.inflate(R.layout.alert_edit_contact_note, null)
        val titleField: RalewayEditText = alertView.findViewById(R.id.titleField)
        val descriptionField: RalewayEditText = alertView.findViewById(R.id.descriptionField)
        val cancelBtn: RalewayButton = alertView.findViewById(R.id.cancelBtn)
        val saveBtn: RalewayButton = alertView.findViewById(R.id.saveBtn)

        val dialog = AlertDialog.Builder(activity)
                .setView(alertView)
                .create()

        cancelBtn.setOnClickListener { dialog.dismiss() }
        saveBtn.setOnClickListener { dialog.dismiss() }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}
