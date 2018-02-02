package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewaySuggestionsField
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 26.01.2018.
 */
class GoalTrackerAdapter(val context: Context): RecyclerView.Adapter<GoalTrackerAdapter.ViewHolder>() {

    var rows: Int = 10
    var delegate: ContactClickListener? = null

    var goalType: GoalType by Delegates.observable(GoalType.reach_outs) { property, oldValue, newValue ->
        when(newValue) {
            GoalType.reach_outs -> rows = 15
            GoalType.follow_ups -> rows = 10
            GoalType.team_reach_outs -> rows = 5
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_follow_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rows
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        holder.rowNumber.text = "${position + 1}"
        if (position == 0) {
            holder.isFilled = true
            holder.searchResultField.text = "Eduard Albu"
        }
        when(goalType) {
            GoalType.reach_outs -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorPrimary))
                holder.scheduleStatus.visibility = View.GONE
                holder.scheduleDaysLabel.visibility = View.GONE
            }
            GoalType.follow_ups -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorGreen))
                if (holder.isFilled) {
                    holder.scheduleStatus.visibility = View.VISIBLE
                    holder.scheduleDaysLabel.visibility = View.VISIBLE
                } else {
                    holder.scheduleStatus.visibility = View.GONE
                    holder.scheduleDaysLabel.visibility = View.GONE
                }
            }
            GoalType.team_reach_outs -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorBlue))
                if (holder.isFilled) {
                    holder.scheduleStatus.visibility = View.VISIBLE
                    holder.scheduleDaysLabel.visibility = View.VISIBLE
                } else {
                    holder.scheduleStatus.visibility = View.GONE
                    holder.scheduleDaysLabel.visibility = View.GONE
                }
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val searchField: RalewaySuggestionsField = itemView.findViewById(R.id.searchField)
        val rowNumber: RalewayTextView = itemView.findViewById(R.id.rowNumberLabel)
        val searchSpinner: Spinner = itemView.findViewById(R.id.searchSpinner)
        val searchResultLayout: LinearLayout = itemView.findViewById(R.id.searchResultLayout)
        val scheduleStatus: RalewayButton = itemView.findViewById(R.id.scheduleStatusBtn)
        val scheduleDaysLabel: RalewayTextView = itemView.findViewById(R.id.followUpDaysRemainedLabel)
        val searchResultField: RalewayTextView = itemView.findViewById(R.id.searchResultLabel)
        val completeBtn: RalewayButton = itemView.findViewById(R.id.completeBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)

        var isFilled: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
            if (newValue) {
                searchField.visibility = View.GONE
                searchResultLayout.visibility = View.VISIBLE
            }
        }

        init {
            completeBtn.setOnClickListener { delegate?.onCompleteClicked() }
            deleteBtn.setOnClickListener { delegate?.onDeleteClicked() }
        }
    }

    interface ContactClickListener {
        fun onCompleteClicked()
        fun onDeleteClicked()
    }
}