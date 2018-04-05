package com.hiloipa.app.hilo.adapter

import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.FilterValue
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 26.02.2018.
 */
class FiltersAdapter(val data: ArrayList<FilterValue>) :
        RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    var delegate: FiltersDelegate? = null

    fun refreshList(data: ArrayList<FilterValue>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_tag_spinner,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = data[position]
        holder.value = value
        holder.nameLabel.text = value.text
        holder.checkBox.isChecked = value.isSelected
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameLabel: RalewayTextView = itemView.findViewById(R.id.textView)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.tagCheckBox)
        lateinit var value: FilterValue

        init {
            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                value.isSelected = checkBox.isChecked

                // if user has selected the all point we need to select all items
                if (value.value.equals("all", true) && value.isSelected) {
                    data.forEach { it.isSelected = true }
                    notifyDataSetChanged()
                }

                if (!value.value.equals("all", true) && !value.isSelected) {
                    val filterValue = data.firstOrNull { it.value.equals("all", true) }
                    if (filterValue != null) {
                        filterValue.isSelected = false
                        val index = data.indexOf(filterValue)
                        notifyItemChanged(index)
                    }
                }

                delegate?.didClickOnTag(value, adapterPosition)
            }
        }
    }

    interface FiltersDelegate {
        fun didClickOnTag(value: FilterValue, position: Int)
    }
}