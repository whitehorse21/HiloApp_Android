/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.ui.contacts.details

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.adapter.FiltersAdapter
import com.hilocrm.app.hilo.models.requests.FilterRequest
import com.hilocrm.app.hilo.models.requests.StandardRequest
import com.hilocrm.app.hilo.models.responses.FilterData
import com.hilocrm.app.hilo.models.responses.FilterValue
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.ui.widget.RalewayButton
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_contact_filter.*

/**
 * Created by eduardalbu on 09.02.2018.
 *
 */
class ContactsFilterFragment() : BottomSheetDialogFragment() {

    lateinit var filterData: FilterData

    companion object {
        val actionFilterContacts = "com.hiloipa.app.hilo.ui.contacts.FILTER_CONTACTS"
        val actionResetFilter = "com.hiloipa.app.hilo.ui.contacts.RESET_FILTER"
        fun newInstance(): ContactsFilterFragment {
            val args = Bundle()
            val fragment = ContactsFilterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.alert_contact_filter, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { this.dismiss() }

        getFilterData()

        filterBtn.setOnClickListener { filterContacts() }

        resetFilterBtn.setOnClickListener {
            val resetIntent = Intent(actionResetFilter)
            LocalBroadcastManager.getInstance(activity!!).sendBroadcast(resetIntent)
            this.dismiss()
        }
    }

    private fun getFilterData() {
        val loading = activity!!.showLoading()
        val request = StandardRequest()
        HiloApp.api().getFilterData(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FilterData> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.filterData = data
                            setupAllSpinnersAndButtons(data)
                        } else this.dismiss()
                    } else {
                        activity!!.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun setupAllSpinnersAndButtons(data: FilterData) {
        // contact type
        updateButtonTextAndTag(contactTypeBtn, data.contactTypes)
        contactTypeBtn.setOnClickListener { showFilterValues(data.contactTypes,
                FilterType.contactType) }

        // group
        updateButtonTextAndTag(groupsBtn, data.contactGroups)
        groupsBtn.setOnClickListener { showFilterValues(data.contactGroups, FilterType.group) }

        // pipeline position
        updateButtonTextAndTag(pipelinePositionButton, data.contactPipelines)
        pipelinePositionButton.setOnClickListener { showFilterValues(data.contactPipelines,
                FilterType.pipelinePosition) }

        // email unsubscribed
        updateButtonTextAndTag(emailUnSubscribedBtn, data.unsubscribe)
        emailUnSubscribedBtn.setOnClickListener { showFilterValues(data.unsubscribe,
                FilterType.emailUnsubscribe) }

        // product purchased
        updateButtonTextAndTag(productPurchasedBtn, data.contactPurhcasedProducts)
        productPurchasedBtn.setOnClickListener { showFilterValues(data.contactPurhcasedProducts,
                FilterType.productPurchased) }

        // recommended products
        updateButtonTextAndTag(productRecommendedBtn, data.contactRecommendedProducts)
        productRecommendedBtn.setOnClickListener { showFilterValues(data.contactRecommendedProducts,
                FilterType.productRecommended) }

        // solution tool yes/no
        updateButtonTextAndTag(solutionToolBtn, data.solutionToolsYesNo)
        solutionToolBtn.setOnClickListener { showFilterValues(data.solutionToolsYesNo,
                FilterType.solutionToolYN) }

        // concerns
        updateButtonTextAndTag(concernBtn, data.contactConcerns)
        concernBtn.setOnClickListener { showFilterValues(data.contactConcerns, FilterType.concerns) }

        // products interested
        updateButtonTextAndTag(productInterestedBtn, data.contactProductsInterests)
        productInterestedBtn.setOnClickListener { showFilterValues(data.contactProductsInterests,
                FilterType.productInterested) }

        // solution tool recommendation
        updateButtonTextAndTag(solutionToolRecommendationBtn, data.contactRecommendedSolutionTools)
        solutionToolRecommendationBtn.setOnClickListener { showFilterValues(data.contactRecommendedSolutionTools,
                FilterType.solutionToolRecommended) }

        // gift
        updateButtonTextAndTag(giftBtn, data.contactGifts)
        giftBtn.setOnClickListener { showFilterValues(data.contactGifts, FilterType.gift) }

        // give it a glow sample
        updateButtonTextAndTag(giveAGlowSampleBtn, data.solutionToolsYesNo)
        giveAGlowSampleBtn.setOnClickListener { showFilterValues(data.solutionToolsYesNo,
                FilterType.glow) }

        // lead temp
        updateButtonTextAndTag(leadTempBtn, data.contactTemp)
        leadTempBtn.setOnClickListener { showFilterValues(data.contactTemp, FilterType.leadTemp) }

        // tags
        updateButtonTextAndTag(tagsBtn, data.contactTags)
        tagsBtn.setOnClickListener { showFilterValues(data.contactTags, FilterType.tags) }

        // there way to call
        updateButtonTextAndTag(threeWayCallBtn, data.thereWayCall)
        threeWayCallBtn.setOnClickListener { showFilterValues(data.thereWayCall,
                FilterType.threeWayCall) }

        // derived by
        updateButtonTextAndTag(derivedByBtn, data.derivedBy)
        derivedByBtn.setOnClickListener { showFilterValues(data.derivedBy, FilterType.derivedBy) }

        // source
        updateButtonTextAndTag(sourceBtn, data.contactSources)
        sourceBtn.setOnClickListener { showFilterValues(data.contactSources, FilterType.source) }

        // state
        updateButtonTextAndTag(stateBtn, data.contactStates)
        stateBtn.setOnClickListener { showFilterValues(data.contactStates, FilterType.state) }
    }

    private fun updateButtonTextAndTag(button: RalewayButton, selectedTags: MutableList<FilterValue>) {
        // build button text and tag strings
        val textBuilder = StringBuilder()
        val tagBuilder = StringBuilder()

        val all = selectedTags.firstOrNull { it.value.equals("all", true) && it.isSelected }
        if (all == null) {
            selectedTags.forEach {
                textBuilder.append(it.text).append(",")
                tagBuilder.append(it.value).append(",")
            }
        } else {
            textBuilder.append(all.text)
            tagBuilder.append(all.value)
        }

        // create strings for text and tag removing coma at the end of the line
        val regex = "(,$)*".toRegex()
        val currentText = regex.replace(textBuilder.toString(), "")
        val currentTag = regex.replace(tagBuilder.toString(), "")

        // set text and tag to the button
        button.text = currentText
        button.tag = currentTag
    }

    private fun showFilterValues(values: ArrayList<FilterValue>, filterType: FilterType) {
        val dialogView = layoutInflater.inflate(R.layout.alert_filters, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.recyclerView)
        val doneBtn: RalewayButton = dialogView.findViewById(R.id.doneBtn)

        val selectedFilters = mutableListOf<FilterValue>()

        val all = values.firstOrNull { it.value.equals("all", true) }
        if (all != null)
            selectedFilters.add(all)

        val adapter = FiltersAdapter(values)
        adapter.delegate = object : FiltersAdapter.FiltersDelegate {
            override fun didClickOnTag(value: FilterValue, position: Int) {
                selectedFilters.clear()
                val allFilters = values.firstOrNull {
                    it.value.equals("all", true) && it.isSelected
                }

                if (allFilters == null) selectedFilters.addAll(values.filter { it.isSelected })
                else selectedFilters.add(allFilters)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        val dialog = AlertDialog.Builder(activity!!)
                .setView(dialogView)
                .create()

        doneBtn.setOnClickListener {
            when (filterType) {
                FilterType.contactType -> updateButtonTextAndTag(contactTypeBtn, selectedFilters)
                FilterType.group -> updateButtonTextAndTag(groupsBtn, selectedFilters)
                FilterType.pipelinePosition -> updateButtonTextAndTag(pipelinePositionButton, selectedFilters)
                FilterType.emailUnsubscribe -> updateButtonTextAndTag(emailUnSubscribedBtn, selectedFilters)
                FilterType.productPurchased -> updateButtonTextAndTag(productPurchasedBtn, selectedFilters)
                FilterType.productRecommended -> updateButtonTextAndTag(productRecommendedBtn, selectedFilters)
                FilterType.solutionToolYN -> updateButtonTextAndTag(solutionToolBtn, selectedFilters)
                FilterType.concerns -> updateButtonTextAndTag(concernBtn, selectedFilters)
                FilterType.productInterested -> updateButtonTextAndTag(productInterestedBtn, selectedFilters)
                FilterType.solutionToolRecommended -> updateButtonTextAndTag(solutionToolRecommendationBtn, selectedFilters)
                FilterType.gift -> updateButtonTextAndTag(giftBtn, selectedFilters)
                FilterType.glow -> updateButtonTextAndTag(giveAGlowSampleBtn, selectedFilters)
                FilterType.leadTemp -> updateButtonTextAndTag(leadTempBtn, selectedFilters)
                FilterType.tags -> updateButtonTextAndTag(tagsBtn, selectedFilters)
                FilterType.threeWayCall -> updateButtonTextAndTag(threeWayCallBtn, selectedFilters)
                FilterType.derivedBy -> updateButtonTextAndTag(derivedByBtn, selectedFilters)
                FilterType.source -> updateButtonTextAndTag(sourceBtn, selectedFilters)
                FilterType.state -> updateButtonTextAndTag(stateBtn, selectedFilters)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun filterContacts() {
        val filterRequest = FilterRequest()
        filterRequest.contactType = contactTypeBtn.tag as String
        filterRequest.group = groupsBtn.tag as String
        filterRequest.pipelinePosition = pipelinePositionButton.tag as String
        filterRequest.emailUnsubscribed = emailUnSubscribedBtn.tag as String
        filterRequest.productPurchased = productPurchasedBtn.tag as String
        filterRequest.recommendedProduct = productRecommendedBtn.tag as String
        filterRequest.solutionToolYesNo = solutionToolBtn.tag as String
        filterRequest.concerns = concernBtn.tag as String
        filterRequest.productInterest = productInterestedBtn.tag as String
        filterRequest.solutionToolRecommended = solutionToolRecommendationBtn.tag as String
        filterRequest.gift = giftBtn.tag as String
        filterRequest.glow = giveAGlowSampleBtn.tag as String
        filterRequest.leadTem = leadTempBtn.tag as String
        filterRequest.threeWayCall = threeWayCallBtn.tag as String
        filterRequest.derivedBy = derivedByBtn.tag as String
        filterRequest.source = sourceBtn.tag as String
        filterRequest.city = cityField.text.toString()
        filterRequest.state = stateBtn.tag as String
        filterRequest.country = countryField.text.toString()

        val filterIntent = Intent(actionFilterContacts)
        val extras = Bundle()
        extras.putParcelable("filter", filterRequest)
        filterIntent.putExtras(extras)
        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(filterIntent)
        this.dismiss()
    }

    enum class FilterType {
        contactType, group, pipelinePosition, emailUnsubscribe, productPurchased, productRecommended,
        solutionToolYN, concerns, productInterested, solutionToolRecommended, gift, glow, leadTemp,
        tags, threeWayCall, derivedBy, source, state
    }
}