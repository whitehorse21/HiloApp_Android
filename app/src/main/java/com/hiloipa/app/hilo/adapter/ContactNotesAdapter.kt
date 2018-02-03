package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 03.02.2018.
 */
class ContactNotesAdapter(val context: Context): RecyclerView.Adapter<ContactNotesAdapter.ViewHolder>() {

    var delegate: ContactNoteDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_contact_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: RalewayTextView = itemView.findViewById(R.id.noteTitleLabel)
        val note: RalewayTextView = itemView.findViewById(R.id.noteLabel)
        val date: RalewayTextView = itemView.findViewById(R.id.noteDateLabel)
        val editBtn: ImageButton = itemView.findViewById(R.id.editNoteBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteNoteBtn)

        init {
            editBtn.setOnClickListener { delegate?.onEditNoteClicked() }
            deleteBtn.setOnClickListener { delegate?.onDeleteNoteClicked() }
        }
    }

    interface ContactNoteDelegate {
        fun onEditNoteClicked()
        fun onDeleteNoteClicked()
    }
}