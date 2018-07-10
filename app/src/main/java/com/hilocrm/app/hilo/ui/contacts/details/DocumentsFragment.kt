/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hilocrm.app.hilo.ui.contacts.details


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.adapter.DocumentsAdapter
import com.hilocrm.app.hilo.models.requests.StandardRequest
import com.hilocrm.app.hilo.models.responses.DocumentResponse
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_documents.*


/**
 * A simple [Fragment] subclass.
 */
class DocumentsFragment : BaseFragment() {

    lateinit var adapter: DocumentsAdapter

    companion object {
        fun newInstance(contactId: String): DocumentsFragment {
            val args = Bundle()
            args.putString("contactId", contactId)
            val fragment = DocumentsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_documents, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DocumentsAdapter(activity!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        getDocumentsList()
    }

    private fun getDocumentsList() {
        val loading = activity!!.showLoading()
        val request = StandardRequest()
        request.contactId = arguments!!.getString("contactId")
        HiloApp.api().getDocuments(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<DocumentResponse> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            adapter.refreshList(data.documents)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }
}
