/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.responses.Campaign
import com.hilocrm.app.hilo.ui.widget.RalewayTextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Eduard Albu on 08 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class CampaignsAdapter(val context: Context): RecyclerView.Adapter<CampaignsAdapter.ViewHolder>() {

    val campaings: ArrayList<Campaign> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    fun refreshCampaigns(campaigns: ArrayList<Campaign>) {
        this.campaings.clear()
        this.campaings.addAll(campaigns)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_campaign,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = campaings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val campaign = campaings[position]
        holder.campaign = campaign
        holder.messageTitleLabel.text = campaign.messageTitle
        holder.campaignLabel.text = campaign.campaign
        holder.sendDateLabel.text = dateFormat.format(campaign.sendDate)
        holder.statusLabel.text = campaign.status
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val messageTitleLabel: RalewayTextView = itemView.findViewById(R.id.messageTitleLabel)
        val campaignLabel: RalewayTextView = itemView.findViewById(R.id.campaignLabel)
        val sendDateLabel: RalewayTextView = itemView.findViewById(R.id.sendDateLabel)
        val statusLabel: RalewayTextView = itemView.findViewById(R.id.emailStatusLabel)
        lateinit var campaign: Campaign
    }
}