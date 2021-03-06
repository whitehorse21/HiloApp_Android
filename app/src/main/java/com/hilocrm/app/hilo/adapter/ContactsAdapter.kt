package com.hilocrm.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.responses.DetailedContact
import com.hilocrm.app.hilo.ui.widget.RalewayTextView
import com.hilocrm.app.hilo.utils.ContactsDelegate
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 01.02.2018.
 */
class ContactsAdapter(val context: Context): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    var delegate: ContactsDelegate? = null

    var contacts: ArrayList<DetailedContact> by
    Delegates.observable(arrayListOf<DetailedContact>()) { property, oldValue, newValue ->
        notifyDataSetChanged()
    }

    fun addContacts(contacts: ArrayList<DetailedContact>) {
        if (this.contacts.isEmpty()) {
            this.contacts.addAll(contacts)
            notifyDataSetChanged()
        } else {
            val lastIndex = this.contacts.lastIndex
            this.contacts.addAll(contacts)
            notifyItemRangeInserted(lastIndex, contacts.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contact = contact
        holder.contactName.text = "${contact.firstName} ${contact.lastName}"
        holder.contactStatus.text = contact.pipelinePos
        // contact temperature
        var color = R.color.colorBlue
        when (contact.tempName) {
            "Cold" -> color = R.color.colorBlue
            "Warm" -> color = R.color.colorDarkYellow
            "Hot" -> color = R.color.colorPrimary
        }
        if (contact.contactNumber != null && contact.contactNumber!!.isNotEmpty()) {
            holder.messageBtn.visibility = VISIBLE
        } else {
            holder.messageBtn.visibility = GONE
        }
        holder.tempIndicator.setBackgroundColor(context.resources.getColor(color))
        holder.checkBox.isChecked = contact.isSelected
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val contactName: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        val contactStatus: RalewayTextView = itemView.findViewById(R.id.contactStatusLabel)
        val tempIndicator: View = itemView.findViewById(R.id.contactStatusIndicator)
        val messageBtn: ImageButton = itemView.findViewById(R.id.messageBtn)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.contactCheckBox)
        lateinit var contact: DetailedContact

        init {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                contact.isSelected = isChecked
            }

            itemView.setOnClickListener {
                delegate?.onContactClicked(contact, adapterPosition)
            }

            editBtn.setOnClickListener {
                delegate?.onEditContactClicked(contact, adapterPosition)
            }

            deleteBtn.setOnClickListener {
                delegate?.onDeleteContactClicked(contact, adapterPosition)
            }

            messageBtn.setOnClickListener {
                delegate?.onSendSmsClicked(contact, adapterPosition)
            }
        }
    }
}