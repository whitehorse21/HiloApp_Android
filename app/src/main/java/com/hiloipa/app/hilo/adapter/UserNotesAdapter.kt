package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.NoteColor
import com.hiloipa.app.hilo.models.responses.Note
import com.hiloipa.app.hilo.models.responses.NoteTag
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eduardalbu on 03.02.2018.
 */
class UserNotesAdapter(val context: Context): RecyclerView.Adapter<UserNotesAdapter.ViewHolder>() {

    var delegate: UserNoteDelegate? = null
    val data: ArrayList<Note> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.getDefault())

    fun refreshData(data: ArrayList<Note>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = data[position]
        holder.note = note
        // update item data
        holder.noteTitle.text = note.title
        holder.noteLabel.text = note.content
        holder.date.text = dateFormat.format(note.time)
        holder.adapter.refreshData(note.tags())
        // update colors
        val noteColor = NoteColor.fromString(note.notColor)
        holder.noteCard.setCardBackgroundColor(context.resources.getColor(noteColor.colorRes()))
        holder.deleteBtn.setImageResource(noteColor.deleteIcon())
        holder.editBtn.setImageResource(noteColor.editIcon())
        holder.noteTitle.setTextColor(context.resources.getColor(noteColor.textColor()))
        holder.noteLabel.setTextColor(context.resources.getColor(noteColor.textColor()))
        holder.date.setTextColor(context.resources.getColor(noteColor.textColor()))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), TagsAdapter.TagDelegate {

        val tagsList: RecyclerView = itemView.findViewById(R.id.tagsList)
        val noteTitle: RalewayTextView = itemView.findViewById(R.id.noteTitleLabel)
        val noteLabel: RalewayTextView = itemView.findViewById(R.id.noteLabel)
        val date: RalewayTextView = itemView.findViewById(R.id.noteDateLabel)
        val noteCard: CardView = itemView.findViewById(R.id.userNoteCard)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteNoteBtn)
        val editBtn: ImageButton = itemView.findViewById(R.id.editNoteBtn)
        var adapter: TagsAdapter
        lateinit var note: Note

        init {
            adapter = TagsAdapter(context)
            adapter.delegate = this
            tagsList.adapter = adapter
            tagsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            editBtn.setOnClickListener { delegate?.onEditNoteClicked(note, adapterPosition) }
            deleteBtn.setOnClickListener { delegate?.onDeleteNoteClicked(note, adapterPosition) }
        }

        override fun onRemoveTagClicked(tag: NoteTag, position: Int) {
            delegate?.onDeleteTagClicked(note, adapterPosition, tag, position)
        }
    }

    interface UserNoteDelegate {
        fun onEditNoteClicked(note: Note, position: Int)
        fun onDeleteNoteClicked(note: Note, position: Int)
        fun onDeleteTagClicked(note: Note, notePosition: Int, tag: NoteTag, position: Int)
    }
}