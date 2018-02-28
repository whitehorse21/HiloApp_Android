/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.DetailedContact
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by Eduard Albu on 28 Февраль 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class SearchContactAdapter(val context: Context): RecyclerView.Adapter<SearchContactAdapter.ViewHolder>() {

    private var contacts: ArrayList<DetailedContact> = arrayListOf()
    var delegate: SearchAdapterDelegate? = null

    fun refreshContacts(contacts: ArrayList<DetailedContact>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_contact_search_result,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val contact = contacts[position]
        holder.contact = contact
        holder.nameLabel.text = "${contact.firstName} ${contact.lastName}"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameLabel: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        lateinit var contact: DetailedContact

        init {
            itemView.setOnClickListener {
                delegate?.onContactSelected(contact, adapterPosition)
            }
        }
    }

    interface SearchAdapterDelegate {
        fun onContactSelected(contact: DetailedContact, position: Int)
    }
}