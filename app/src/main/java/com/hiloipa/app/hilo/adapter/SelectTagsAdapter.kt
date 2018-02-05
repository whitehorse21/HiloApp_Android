package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.TagColor
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 05.02.2018.
 */
class SelectTagsAdapter(val context: Context): RecyclerView.Adapter<SelectTagsAdapter.ViewHolder>() {

    var delegate: SelectTagDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_select_tag, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 14
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val tagColor = TagColor.values()[position]
        holder.tagName.setBackgroundColor(context.resources.getColor(tagColor.colorRes()))
        holder.tagName.setTextColor(context.resources.getColor(tagColor.textColor()))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), CompoundButton.OnCheckedChangeListener {

        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.tagCheckBox)
        val tagName: RalewayTextView = itemView.findViewById(R.id.tagNameLabel)
        val removeTagBtn: ImageButton = itemView.findViewById(R.id.removeTagBtn)

        init {
            removeTagBtn.setOnClickListener { delegate?.onRemoveTagClicked() }
            itemView.setOnClickListener { checkBox.isChecked = !checkBox.isChecked }
            checkBox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            delegate?.onTagSelectedChanged(isChecked)
        }
    }

    interface SelectTagDelegate {
        fun onRemoveTagClicked()
        fun onTagSelectedChanged(isSelected: Boolean)
    }
}