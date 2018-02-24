package com.hiloipa.app.hilo.adapter

import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.Tag
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 24.02.2018.
 */
class TagsSpinnerAdapter(val tags: List<Tag>): BaseAdapter() {

    var delegate: TagSpinnerDelegate? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null)
            view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_tag_spinner,
                    parent, false)

        val textView: RalewayTextView = view!!.findViewById(R.id.textView)
        val checkBox: AppCompatCheckBox = view.findViewById(R.id.tagCheckBox)

        textView.text = tags[position].name
        checkBox.isChecked = tags[position].isSelected

        view.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            tags[position].isSelected = checkBox.isChecked
            delegate?.didClickOnTag(tags[position], position)
        }

        return view
    }

    override fun getItem(position: Int): Any = tags[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = tags.size

    interface TagSpinnerDelegate {
        fun didClickOnTag(tag: Tag, position: Int)
    }
}