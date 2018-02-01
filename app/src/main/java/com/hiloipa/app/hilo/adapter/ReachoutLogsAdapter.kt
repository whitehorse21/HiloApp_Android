package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 01.02.2018.
 */
class ReachoutLogsAdapter(val context: Context): RecyclerView.Adapter<ReachoutLogsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_reachout_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        holder.contactName.text = "Eduard Albu"
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val expandBtn: ImageButton = itemView.findViewById(R.id.reachoutExpandBtn)
        val contactName: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        val reachTypeLabel: RalewayTextView = itemView.findViewById(R.id.reachoutTypeLabel)
        val dateLabel: RalewayTextView = itemView.findViewById(R.id.dateLabel)
        val commentsLayout: LinearLayout = itemView.findViewById(R.id.commentsHolderLayout)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)

        init {
            expandBtn.setOnClickListener {
                if (expandBtn.isSelected) {
                    this.commentsLayout.visibility = View.GONE
                    expandBtn.isSelected = false
                } else {
                    this.commentsLayout.visibility = View.VISIBLE
                    expandBtn.isSelected = true
                }
            }
        }
    }
}