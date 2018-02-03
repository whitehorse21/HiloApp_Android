package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 03.02.2018.
 */
class EmailTemplatesAdapter(val context: Context): RecyclerView.Adapter<EmailTemplatesAdapter.ViewHolder>() {

    var delegate: EmailTemplateDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_email_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val templateTitle: RalewayTextView = itemView.findViewById(R.id.templateTitleLabel)
        val previewBtn: RalewayButton = itemView.findViewById(R.id.previewTemplateBtn)

        init {
            previewBtn.setOnClickListener {
                delegate?.onPreviewTemplateClicked()
            }
        }
    }

    interface EmailTemplateDelegate {
        fun onPreviewTemplateClicked()
    }
}