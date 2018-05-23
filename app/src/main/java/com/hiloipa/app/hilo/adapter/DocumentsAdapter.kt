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
import com.hiloipa.app.hilo.models.responses.Document
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eduard Albu on 08 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class DocumentsAdapter(val context: Context) : RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    var documents: ArrayList<Document> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    fun refreshList(documents: ArrayList<Document>) {
        this.documents.clear()
        this.documents.addAll(documents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_document, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = documents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = documents[position]
        holder.document = document
        holder.titleLabel.text = document.title
        holder.uploadDateLabel.text = dateFormat.format(document.uploadDate)
        holder.actionLabel.text = document.url
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleLabel: RalewayTextView = itemView.findViewById(R.id.documentTitleLabel)
        val uploadDateLabel: RalewayTextView = itemView.findViewById(R.id.uploadDateLabel)
        val actionLabel: RalewayTextView = itemView.findViewById(R.id.actionLabel)
        lateinit var document: Document
    }
}