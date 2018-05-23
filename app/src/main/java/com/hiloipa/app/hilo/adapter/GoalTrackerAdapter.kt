package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.responses.Contact
import com.hiloipa.app.hilo.models.responses.DetailedContact
import com.hiloipa.app.hilo.models.responses.GoalTrackerResponse
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 26.01.2018.
 */
class GoalTrackerAdapter(val context: Context) : RecyclerView.Adapter<GoalTrackerAdapter.ViewHolder>() {

    var rows: Int = 10
    var delegate: ContactClickListener? = null

    var contacts: ArrayList<DetailedContact> = arrayListOf()

    lateinit var data: GoalTrackerResponse

    var goalType: GoalType by Delegates.observable(GoalType.reach_outs) { property, oldValue, newValue ->
        when (newValue) {
            GoalType.reach_outs -> rows = data.goalPlan.reachOuts
            GoalType.follow_ups -> rows = data.goalPlan.followUps
            GoalType.team_reach_outs -> rows = data.goalPlan.teamReachOuts
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_follow_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rows
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rowNumber.text = "${position + 1}"
        // update item data
        when (goalType) {
            GoalType.reach_outs -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorPrimary))
                val contact = data.reachOuts.reachOuts.getOrNull(position)
                if (contact != null) {
                    holder.contact = contact
                    holder.searchResultField.text = contact.name
                    holder.isFilled = true
                } else {
                    holder.isFilled = false
                }
                holder.scheduleStatus.visibility = View.GONE
                holder.scheduleDaysLabel.visibility = View.GONE
            }

            GoalType.follow_ups -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorGreen))
                val contact = data.followUps.followUpContacts.getOrNull(position)
                if (contact != null) {
                    holder.contact = contact
                    holder.searchResultField.text = contact.name
                    holder.isFilled = true
                    if (contact.badge.isNotEmpty()) {
                        holder.scheduleStatus.visibility = View.VISIBLE
                        holder.scheduleStatus.text = contact.badge
                    } else {
                        holder.scheduleStatus.visibility = View.GONE
                    }
                } else holder.isFilled = false
            }

            GoalType.team_reach_outs -> {
                holder.rowNumber.setTextColor(context.resources.getColor(R.color.colorBlue))
                val contact = data.teamReachOuts.teamReachoutContacts.getOrNull(position)
                if (contact != null) {
                    holder.contact = contact
                    holder.searchResultField.text = contact.name
                    holder.isFilled = true
                } else {
                    holder.isFilled = false
                }
                holder.scheduleStatus.visibility = View.GONE
                holder.scheduleDaysLabel.visibility = View.GONE
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchField: RalewayButton = itemView.findViewById(R.id.searchField)
        val rowNumber: RalewayTextView = itemView.findViewById(R.id.rowNumberLabel)
        val searchSpinner: Spinner = itemView.findViewById(R.id.searchSpinner)
        val searchResultLayout: LinearLayout = itemView.findViewById(R.id.searchResultLayout)
        val scheduleStatus: RalewayButton = itemView.findViewById(R.id.scheduleStatusBtn)
        val scheduleDaysLabel: RalewayTextView = itemView.findViewById(R.id.followUpDaysRemainedLabel)
        val searchResultField: RalewayTextView = itemView.findViewById(R.id.searchResultLabel)
        val completeBtn: RalewayButton = itemView.findViewById(R.id.completeBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        lateinit var contact: Contact

        var isFilled: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
            if (newValue) {
                searchField.visibility = View.GONE
                searchResultLayout.visibility = View.VISIBLE
            } else {
                searchField.visibility = View.VISIBLE
                searchResultLayout.visibility = View.GONE
            }
        }

        init {
            completeBtn.setOnClickListener { delegate?.onCompleteClicked(contact, adapterPosition) }
            deleteBtn.setOnClickListener { delegate?.onDeleteClicked(contact, adapterPosition) }
            itemView.setOnClickListener { delegate?.onContactClicked(contact, adapterPosition) }

            // setup search field suggestions
            val contactsNames = mutableListOf<String>()
            contacts.forEach { contactsNames.add(it.name) }
            searchField.setOnClickListener { delegate?.didWantToSearchContact() }
        }
    }

    interface ContactClickListener {
        fun onCompleteClicked(contact: Contact, position: Int)
        fun onDeleteClicked(contact: Contact, position: Int)
        fun onContactClicked(contact: Contact, position: Int)
        fun didWantToSearchContact()
    }
}