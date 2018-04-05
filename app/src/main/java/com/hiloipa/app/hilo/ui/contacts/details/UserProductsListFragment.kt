/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.contacts.details

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactProductsAdapter
import com.hiloipa.app.hilo.models.requests.UnassignProduct
import com.hiloipa.app.hilo.models.responses.ContactProduct
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_products_list.*

/**
 * Created by eduardalbu on 03.02.2018.
 */
class UserProductsListFragment: BottomSheetDialogFragment(), ContactProductsAdapter.UserProductDelegate {

    lateinit var adapter: ContactProductsAdapter

    companion object {
        const val actionUpdateProducts = "com.hiloipa.app.hilo.ui.contacts.UPDATE_CONTACT_PRODUCTS"

        fun newInstace(products: ArrayList<ContactProduct>, contactId: String): UserProductsListFragment {
            val args = Bundle()
            args.putParcelableArrayList("products", products)
            args.putString("contactId", contactId)
            val fragment = UserProductsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater?.inflate(R.layout.fragment_user_products_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { this.dismiss() }
        adapter = ContactProductsAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter.refreshProducts(arguments!!.getParcelableArrayList("products"))
    }

    override fun onUnasignProductClicked(product: ContactProduct, position: Int) {
        val alert = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.hilo))
                .setMessage(getString(R.string.confirm_unassign_product))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    unassigProduct(product, position)
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        alert.show()
    }

    private fun unassigProduct(product: ContactProduct, position: Int) {
        val loading = activity!!.showLoading()
        val request = UnassignProduct()
        request.contactId = arguments!!.getString("contactId")
        request.productId = "${product.id}"
        request.productType = product.assignedType
        HiloApp.api().unassignProduct(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        LocalBroadcastManager.getInstance(activity!!)
                                .sendBroadcastSync(Intent(actionUpdateProducts))
                        adapter.products.remove(product)
                        adapter.notifyItemRemoved(position)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }
}