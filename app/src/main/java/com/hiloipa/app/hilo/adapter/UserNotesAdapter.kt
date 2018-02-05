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
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 03.02.2018.
 */
class UserNotesAdapter(val context: Context): RecyclerView.Adapter<UserNotesAdapter.ViewHolder>() {

    var delegate: UserNoteDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_user_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 14
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return

        // update item view colors according to card color
        val cardColor = NoteColor.values()[position]
        holder.noteCard.setCardBackgroundColor(context.resources.getColor(cardColor.colorRes()))
        holder.noteTitle.setTextColor(context.resources.getColor(cardColor.textColor()))
        holder.note.setTextColor(context.resources.getColor(cardColor.textColor()))
        holder.date.setTextColor(context.resources.getColor(cardColor.textColor()))
        holder.deleteBtn.setImageResource(cardColor.deleteIcon())
        holder.editBtn.setImageResource(cardColor.editIcon())
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), TagsAdapter.TagDelegate {

        val tagsList: RecyclerView = itemView.findViewById(R.id.tagsList)
        val noteTitle: RalewayTextView = itemView.findViewById(R.id.noteTitleLabel)
        val note: RalewayTextView = itemView.findViewById(R.id.noteLabel)
        val date: RalewayTextView = itemView.findViewById(R.id.noteDateLabel)
        val noteCard: CardView = itemView.findViewById(R.id.userNoteCard)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteNoteBtn)
        val editBtn: ImageButton = itemView.findViewById(R.id.editNoteBtn)
        var adapter: TagsAdapter

        init {
            adapter = TagsAdapter(context)
            adapter.delegate = this
            tagsList.adapter = adapter
            tagsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            editBtn.setOnClickListener { delegate?.onEditNoteClicked() }
            deleteBtn.setOnClickListener { delegate?.onDeleteNoteClicked() }
        }

        override fun onRemoveTagClicked() {

        }
    }

    interface UserNoteDelegate {
        fun onEditNoteClicked()
        fun onDeleteNoteClicked()
    }
}