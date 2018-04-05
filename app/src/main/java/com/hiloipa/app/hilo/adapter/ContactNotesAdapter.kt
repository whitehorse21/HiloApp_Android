package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.ContactNote
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eduardalbu on 03.02.2018.
 */
class ContactNotesAdapter(val context: Context): RecyclerView.Adapter<ContactNotesAdapter.ViewHolder>() {

    var delegate: ContactNoteDelegate? = null
    var notes: ArrayList<ContactNote> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.ENGLISH)

    fun refreshNotes(notes: ArrayList<ContactNote>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contact_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.note = note
        holder.noteLabel.text = String(Base64.decode(note.content, Base64.DEFAULT))
        holder.title.text = note.title
        holder.date.text = dateFormat.format(note.updateTime)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: RalewayTextView = itemView.findViewById(R.id.noteTitleLabel)
        val noteLabel: RalewayTextView = itemView.findViewById(R.id.noteLabel)
        val date: RalewayTextView = itemView.findViewById(R.id.noteDateLabel)
        val editBtn: ImageButton = itemView.findViewById(R.id.editNoteBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteNoteBtn)
        lateinit var note: ContactNote

        init {
            editBtn.setOnClickListener { delegate?.onEditNoteClicked(note, adapterPosition) }
            deleteBtn.setOnClickListener { delegate?.onDeleteNoteClicked(note, adapterPosition) }
        }
    }

    interface ContactNoteDelegate {
        fun onEditNoteClicked(note: ContactNote, position: Int)
        fun onDeleteNoteClicked(note: ContactNote, position: Int)
    }
}