package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.TagColor
import com.hiloipa.app.hilo.models.responses.NoteTag
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 03.02.2018.
 */
class TagsAdapter(val context: Context): RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    var delegate: TagDelegate? = null
    val tags: ArrayList<NoteTag> = arrayListOf()

    fun refreshData(data: ArrayList<NoteTag>) {
        this.tags.clear()
        this.tags.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_tag, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tags[position]
        holder.tag = tag
        holder.tagName.text = tag.name
        // colors
        val tagColor = tag.color()
        holder.tagCard.setCardBackgroundColor(context.resources.getColor(tagColor.colorRes()))
        holder.tagName.setTextColor(context.resources.getColor(tagColor.textColor()))
        holder.removeTagBtn.setImageResource(tagColor.icon())
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tagName: RalewayTextView = itemView.findViewById(R.id.tagNameLabel)
        val tagCard: CardView = itemView.findViewById(R.id.tagCard)
        val removeTagBtn: ImageButton = itemView.findViewById(R.id.removeTagBtn)
        lateinit var tag: NoteTag

        init {
            removeTagBtn.setOnClickListener { delegate?.onRemoveTagClicked(tag, adapterPosition) }
        }
    }

    interface TagDelegate {
        fun onRemoveTagClicked(tag: NoteTag, position: Int)
    }
}