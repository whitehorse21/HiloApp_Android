package com.hiloipa.app.hilo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.MessageScript
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 26.02.2018.
 */
class TextMessageScriptAdapter(val scripts: ArrayList<MessageScript>):
        RecyclerView.Adapter<TextMessageScriptAdapter.ViewHolder>() {

    var delegate: ScriptDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_script_message,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = scripts.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val script = scripts[position]
        holder.script = script
        holder.titleLabel.text = script.title
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleLabel: RalewayTextView = itemView.findViewById(R.id.scriptNameLabel)
        private val insertBtn: RalewayButton = itemView.findViewById(R.id.insertTextBtn)
        lateinit var script: MessageScript

        init {
            insertBtn.setOnClickListener {
                delegate?.onInsertTextClicked(script, adapterPosition)
            }
        }
    }

    interface ScriptDelegate {
        fun onInsertTextClicked(script: MessageScript, position: Int)
    }
}