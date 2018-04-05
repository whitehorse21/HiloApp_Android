/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.contacts.details


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.CampaignsAdapter
import com.hiloipa.app.hilo.models.requests.AssignCampaignRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.CampaignsData
import com.hiloipa.app.hilo.models.responses.CampaignsResponse
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contact_campaigns.*


/**
 * A simple [Fragment] subclass.
 */
class ContactCampaignsFragment : Fragment() {

    lateinit var adapter: CampaignsAdapter
    lateinit var contactCampaigns: CampaignsResponse

    companion object {
        fun newInstance(contactId: String): ContactCampaignsFragment {
            val args = Bundle()
            args.putString("contactId", contactId)
            val fragment = ContactCampaignsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contact_campaigns, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CampaignsAdapter(activity!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false

        assignBtn.setOnClickListener {
            showEmailCampaignsDialog()
        }

        getContactCampaigns()
    }

    private fun getContactCampaigns() {
        val loading = activity!!.showLoading()
        val request = StandardRequest()
        request.contactId = arguments!!.getString("contactId")
        HiloApp.api().getCampaigns(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<CampaignsResponse> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.contactCampaigns = data
                            adapter.refreshCampaigns(data.campaigs)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun showEmailCampaignsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_assign_campaign, null)
        val campaignsBtn: RalewayButton = dialogView.findViewById(R.id.emailCampaignsBtn)
        val spinner: Spinner = dialogView.findViewById(R.id.emailCampaignsSpinner)
        val assignBtn: RalewayButton = dialogView.findViewById(R.id.updateBtn)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.backButton)

        val loading = activity!!.showLoading()
        HiloApp.api().getCampaignData(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<CampaignsData> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            val campaigns = mutableListOf<String>()
                            data.campaigns.forEach { campaigns.add(it.name) }
                            spinner.adapter = ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_dropdown_item, campaigns)
                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    val campaign = data.campaigns[position]
                                    campaignsBtn.text = campaign.name
                                    campaignsBtn.tag = campaign.id
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                            campaignsBtn.setOnClickListener { spinner.performClick() }

                            val dialog = AlertDialog.Builder(activity!!).setView(dialogView).create()

                            assignBtn.setOnClickListener {
                                val contacts = arguments!!.getString("contactId")

                                val assignLoading = activity!!.showLoading()
                                val request = AssignCampaignRequest()
                                request.contactId = contacts
                                request.campaignId = "${campaignsBtn.tag as Int}"
                                HiloApp.api().assignCampaign(request)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ response: HiloResponse<String> ->
                                            assignLoading.dismiss()
                                            if (response.status.isSuccess()) {
                                                dialog.dismiss()
                                                getContactCampaigns()
                                                activity!!.showExplanation(title = getString(R.string.success),
                                                        message = response.message)
                                            } else activity!!.showExplanation(message = response.message)
                                        }, { error: Throwable ->
                                            assignLoading.dismiss()
                                            error.printStackTrace()
                                            activity!!.showExplanation(message = error.localizedMessage)
                                        })
                            }

                            backBtn.setOnClickListener { dialog.dismiss() }
                            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            dialog.show()
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }
}
