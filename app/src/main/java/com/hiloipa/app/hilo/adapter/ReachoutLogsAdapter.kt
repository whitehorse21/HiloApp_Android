package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.ReachOutLog
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eduardalbu on 01.02.2018.
 */
class ReachoutLogsAdapter(val context: Context): RecyclerView.Adapter<ReachoutLogsAdapter.ViewHolder>() {

    private var logs: MutableList<ReachOutLog> = mutableListOf()
    var delegate: ReachOutDelegate? = null
    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.ENGLISH)

    fun refreshLogList(logs: ArrayList<ReachOutLog>) {
        this.logs.clear()
        this.logs.addAll(logs)
        notifyDataSetChanged()
    }

    fun addLogs(logs: ArrayList<ReachOutLog>) {
        val lastIndex = this.logs.lastIndex
        this.logs.addAll(logs)
        notifyItemRangeInserted(lastIndex, logs.size)
    }

    fun deleteLog(log: ReachOutLog) {
        val index = logs.indexOf(log)
        logs.remove(log)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_reachout_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val log = logs[position]
        holder.log = log
        holder.contactName.text = log.contactName
        holder.reachTypeLabel.text = context.getString(R.string.reach_type_s, log.typeName)
        if (log.description.isNotEmpty()) {
            val data = Base64.decode(log.description, Base64.DEFAULT)
            holder.commentLabel.setText(String(data, Charset.forName("utf-8")))
        } else {
            holder.commentLabel.setText(log.description)
        }
        holder.dateLabel.text = dateFormat.format(log.sortTime)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val expandBtn: ImageButton = itemView.findViewById(R.id.reachoutExpandBtn)
        val contactName: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        val reachTypeLabel: RalewayTextView = itemView.findViewById(R.id.reachoutTypeLabel)
        val dateLabel: RalewayTextView = itemView.findViewById(R.id.dateLabel)
        val commentsLayout: LinearLayout = itemView.findViewById(R.id.commentsHolderLayout)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val commentLabel: RalewayEditText = itemView.findViewById(R.id.logCommentLabel)
        lateinit var log: ReachOutLog

        init {
            itemView.setOnClickListener {
                if (expandBtn.isSelected) {
                    this.commentsLayout.visibility = View.GONE
                    expandBtn.isSelected = false
                } else {
                    this.commentsLayout.visibility = View.VISIBLE
                    expandBtn.isSelected = true
                }
            }

            editBtn.setOnClickListener { delegate?.onEditLogClicked(log, adapterPosition) }

            deleteBtn.setOnClickListener { delegate?.onDeleteLogClicked(log, adapterPosition) }

            contactName.setOnClickListener {
                delegate?.onContactNameClicked(log, adapterPosition)
            }
        }
    }

    interface ReachOutDelegate {
        fun onContactNameClicked(log: ReachOutLog, position: Int)
        fun onEditLogClicked(log: ReachOutLog, position: Int)
        fun onDeleteLogClicked(log: ReachOutLog, position: Int)
    }
}