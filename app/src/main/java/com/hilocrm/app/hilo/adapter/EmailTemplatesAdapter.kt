package com.hilocrm.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.responses.Template
import com.hilocrm.app.hilo.ui.widget.RalewayButton
import com.hilocrm.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 03.02.2018.
 */
class EmailTemplatesAdapter(val context: Context): RecyclerView.Adapter<EmailTemplatesAdapter.ViewHolder>() {

    var delegate: EmailTemplateDelegate? = null
    var templates: ArrayList<Template> = arrayListOf()

    fun refreshList(templates: ArrayList<Template>) {
        this.templates.clear()
        this.templates.addAll(templates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_email_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val template = templates[position]
        holder.template = template
        holder.templateTitle.text = template.name
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val templateTitle: RalewayTextView = itemView.findViewById(R.id.templateTitleLabel)
        val previewBtn: RalewayButton = itemView.findViewById(R.id.previewTemplateBtn)
        lateinit var template: Template

        init {
            previewBtn.setOnClickListener {
                delegate?.onPreviewTemplateClicked(template, adapterPosition)
            }
        }
    }

    interface EmailTemplateDelegate {
        fun onPreviewTemplateClicked(template: Template, position: Int)
    }
}