package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.ContactWrapper
import com.hiloipa.app.hilo.models.responses.DetailedContact
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.ContactsDelegate
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 01.02.2018.
 */
class ContactsAdapter(val context: Context): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    var delegate: ContactsDelegate? = null

    var contactWrapper: ArrayList<ContactWrapper> by
    Delegates.observable(arrayListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var contacts: MutableList<DetailedContact> = arrayListOf()
        get() = contactWrapper.map { it.detailContact }.toMutableList()

    fun addContacts(contacts: List<ContactWrapper>) {
        if (this.contactWrapper.isEmpty()) {
            this.contactWrapper.addAll(contacts)
            notifyDataSetChanged()
        } else {
            val lastIndex = this.contactWrapper.lastIndex
            this.contactWrapper.addAll(contacts)
            notifyItemRangeInserted(lastIndex, contacts.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contactWrapper.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactWrapper[position].detailContact
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
        val alternatephns = contactWrapper[position].contactDetails.alternatephns
        if (contact.contactNumber != null && contact.contactNumber.isNotEmpty() && alternatephns != null && alternatephns.isNotEmpty()) {
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