package com.hilocrm.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.TagColor
import com.hilocrm.app.hilo.models.responses.NoteTag
import com.hilocrm.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 05.02.2018.
 */
class SelectTagsAdapter(val context: Context): RecyclerView.Adapter<SelectTagsAdapter.ViewHolder>() {

    var delegate: SelectTagDelegate? = null
    val tags: ArrayList<NoteTag> = arrayListOf()

    fun refreshData(tags: ArrayList<NoteTag>) {
        this.tags.clear()
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_select_tag, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tags[position]
        holder.tag = tag
        val tagColor = tag.color()
        holder.tagName.text = tag.name
        holder.checkBox.isChecked = tag.isSelected
        holder.tagName.setBackgroundColor(context.resources.getColor(tagColor.colorRes()))
        holder.tagName.setTextColor(context.resources.getColor(tagColor.textColor()))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.tagCheckBox)
        val tagName: RalewayTextView = itemView.findViewById(R.id.tagNameLabel)
        val removeTagBtn: ImageButton = itemView.findViewById(R.id.removeTagBtn)
        lateinit var tag: NoteTag

        init {
            removeTagBtn.setOnClickListener { delegate?.onRemoveTagClicked(tag, adapterPosition) }
            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                tag.isSelected = checkBox.isChecked
                delegate?.onTagSelectedChanged(tag, adapterPosition)
            }
        }
    }

    interface SelectTagDelegate {
        fun onRemoveTagClicked(tag: NoteTag, position: Int)
        fun onTagSelectedChanged(tag: NoteTag, position: Int)
    }
}