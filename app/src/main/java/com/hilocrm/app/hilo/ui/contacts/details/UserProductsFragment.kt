/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.ui.contacts.details


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.requests.StandardRequest
import com.hilocrm.app.hilo.models.responses.ContactProduct
import com.hilocrm.app.hilo.models.responses.ContactProducts
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_products.*


/**
 * A simple [Fragment] subclass.
 */
class UserProductsFragment : BaseFragment(), View.OnClickListener {

    lateinit var contactProducts: ContactProducts

    companion object {
        fun newInstace(contactId: String): UserProductsFragment {
            val args = Bundle()
            args.putString("contactId", contactId)
            val fragment = UserProductsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_user_products, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filter = IntentFilter(UserProductsListFragment.actionUpdateProducts)
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(broadcastReceiver, filter)

        purchasedProductsBtn.setOnClickListener(this)
        recomendedProductsBtn.setOnClickListener(this)
        interestedProductsBtn.setOnClickListener(this)
        toolRecomendedProductsBtn.setOnClickListener(this)

        getContactProducts()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when(intent.action) {
                UserProductsListFragment.actionUpdateProducts -> getContactProducts()
            }
        }
    }

    private fun getContactProducts() {
        val loading = activity!!.showLoading()
        val request = StandardRequest()
        request.contactId = arguments!!.getString("contactId")
        HiloApp.api().getContactProducts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ContactProducts> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            updateUI(data)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateUI(contactProducts: ContactProducts) {
        this.contactProducts = contactProducts
        purchasedProductsBtn.text = getString(R.string.products_purchased_d, contactProducts.purchased.size)
        purchasedProductsBtn.tag = contactProducts.purchased
        recomendedProductsBtn.text = getString(R.string.recomended_products_d, contactProducts.recommended.size)
        recomendedProductsBtn.tag = contactProducts.recommended
        interestedProductsBtn.text = getString(R.string.product_interested_d, contactProducts.interested.size)
        interestedProductsBtn.tag = contactProducts.interested
        toolRecomendedProductsBtn.text = getString(R.string.solution_tool_recomended_product_d,
                contactProducts.solutionTool?.size ?: 0)
        toolRecomendedProductsBtn.tag = contactProducts.solutionTool
    }

    override fun onClick(v: View) {
        if (v.tag != null) {
            val products = v.tag as ArrayList<ContactProduct>
            if (products.isNotEmpty()) {
                UserProductsListFragment.newInstace(products, arguments!!.getString("contactId"))
                        .show(childFragmentManager, "ProductsListFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(broadcastReceiver)
    }
}
