package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 05.02.2018.
 */
class ScriptsAdapter(val context: Context): RecyclerView.Adapter<ScriptsAdapter.ViewHolder>() {

    var delegate: ScriptDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_script, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 15

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: RalewayTextView = itemView.findViewById(R.id.scriptNameLabel)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)

        init {
            itemView.setOnClickListener { delegate?.onScriptClicked() }
            editBtn.setOnClickListener { delegate?.onEditScriptClicked() }
            deleteBtn.setOnClickListener { delegate?.onDeleteScriptClicked() }
        }
    }

    interface ScriptDelegate {
        fun onScriptClicked()
        fun onEditScriptClicked()
        fun onDeleteScriptClicked()
    }
}