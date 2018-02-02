package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.ContactsDelegate

/**
 * Created by eduardalbu on 01.02.2018.
 */
class ContactsAdapter(val context: Context): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    var delegate: ContactsDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        holder.contactName.text = "Eduard Albu"
        holder.contactStatus.text = "Contacted"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val contactName: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        val contactStatus: RalewayTextView = itemView.findViewById(R.id.contactStatusLabel)
        val tempIndicator: View = itemView.findViewById(R.id.contactStatusIndicator)
        val messageBtn: ImageButton = itemView.findViewById(R.id.messageBtn)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.contactCheckBox)

        init {
            itemView.setOnClickListener {
                delegate?.onContactClicked()
            }

            editBtn.setOnClickListener {
                delegate?.onEditContactClicked()
            }

            deleteBtn.setOnClickListener {
                delegate?.onDeleteContactClicked()
            }

            messageBtn.setOnClickListener {
                delegate?.onSendSmsClicked()
            }
        }
    }
}