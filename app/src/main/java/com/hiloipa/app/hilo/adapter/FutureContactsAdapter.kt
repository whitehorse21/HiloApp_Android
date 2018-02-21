package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.responses.Contact
import com.hiloipa.app.hilo.models.responses.FollowUpContact
import com.hiloipa.app.hilo.models.responses.ReachOutContact
import com.hiloipa.app.hilo.models.responses.TeamReachOutContact
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 21.02.2018.
 *
 */
class FutureContactsAdapter(val context: Context, val goalType: GoalType): RecyclerView.Adapter<FutureContactsAdapter.ViewHolder>() {

    var contacts: MutableList<Contact> = mutableListOf()
    var delegate: GoalTrackerAdapter.ContactClickListener? = null

    fun <T: Contact> updateAdapterData(contacts: ArrayList<T>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_future_contact,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        when (goalType) {
            GoalType.reach_outs -> {
                val contact = contacts[position] as ReachOutContact
                holder.contactName.text = contact.name
                holder.position.text = "${contact.slotId}"
                holder.position.setTextColor(context.resources.getColor(R.color.tagGreen600))
                holder.badge.visibility = View.GONE
                holder.daysLabel.visibility = View.GONE
            }

            GoalType.follow_ups -> {
                val contact = contacts[position] as FollowUpContact
                holder.contactName.text = contact.name
                holder.position.text = "${contact.slotId}"
                holder.position.setTextColor(context.resources.getColor(R.color.tagGreen600))

                if (contact.badge.isNotEmpty()) {
                    holder.badge.visibility = View.VISIBLE
                    holder.badge.text = contact.badge
                }

                if (contact.days != null && contact.days.isNotEmpty()) {
                    holder.daysLabel.visibility = View.VISIBLE
                    holder.daysLabel.text = contact.days
                }
            }

            GoalType.team_reach_outs -> {
                val contact = contacts[position] as TeamReachOutContact
                holder.contactName.text = contact.name
                holder.position.text = "${contact.slotId}"
                holder.position.setTextColor(context.resources.getColor(R.color.tagGreen600))

                if (contact.badge != null && contact.badge.isNotEmpty()) {
                    holder.badge.visibility = View.VISIBLE
                    holder.badge.text = contact.badge
                }

                if (contact.days != null && contact.days.isNotEmpty()) {
                    holder.daysLabel.visibility = View.VISIBLE
                    holder.daysLabel.text = contact.days
                }
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val position: RalewayTextView = itemView.findViewById(R.id.rowNumberLabel)
        val contactName: RalewayTextView = itemView.findViewById(R.id.contactNameLabel)
        val daysLabel: RalewayTextView = itemView.findViewById(R.id.followUpDaysRemainedLabel)
        val completeBtn: RalewayButton = itemView.findViewById(R.id.completeBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val badge: RalewayButton = itemView.findViewById(R.id.badgeBtn)

        init {
            itemView.setOnClickListener { delegate?.onContactClicked() }
            deleteBtn.setOnClickListener { delegate?.onDeleteClicked() }
            completeBtn.setOnClickListener { delegate?.onCompleteClicked() }
        }
    }
}