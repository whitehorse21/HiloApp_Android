/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.tracker

import android.app.DialogFragment
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.SearchContactAdapter
import com.hiloipa.app.hilo.models.requests.ContactsListRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.displaySize
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search_contacts.*

/**
 * Created by Eduard Albu on 21 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class FragmentSearchGoalContact: DialogFragment(), SearchContactAdapter.SearchAdapterDelegate {

    var delegate: SearchGoalDelegate? = null
    lateinit var adapter: SearchContactAdapter<SearchContact>
    lateinit var searchUrl: String

    companion object {
        fun newInstance(delegate: SearchGoalDelegate? = null, searchUrl: String): FragmentSearchGoalContact {
            val args = Bundle()
            args.putString("searchUrl", searchUrl)
            val fragment = FragmentSearchGoalContact()
            fragment.arguments = args
            fragment.delegate = delegate
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        val displaySize = activity.displaySize()
        val width = displaySize.first - 200
        val height = displaySize.second - 300
        dialog.window!!.setLayout(width, height)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_search_contacts, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { this.dismiss() }
        adapter = SearchContactAdapter(activity)
        adapter.delegate = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        val handler = Handler()
        searchUrl = arguments.getString("searchUrl")
        val requests: MutableList<Observable<HiloResponse<ArrayList<SearchContact>>>> = mutableListOf()
        searchField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                requests.clear()
                progressBar.visibility = View.VISIBLE
                val query = s.toString()
                val request = ContactsListRequest()
                request.query = query
                requests.add(HiloApp.api().searchContacts(searchUrl, request))
                handler.postDelayed({
                    getAllContactsFromServer(requests)
                }, 1000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        val request = ContactsListRequest()
        request.query = ""
        progressBar.visibility = View.VISIBLE
        getAllContactsFromServer(mutableListOf(HiloApp.api().searchContacts(searchUrl, request)))
    }

    private fun getAllContactsFromServer(requests: MutableList<Observable<HiloResponse<ArrayList<SearchContact>>>>) {
        Observable.merge(requests)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { progressBar.visibility = View.GONE }
                .subscribe({ response: HiloResponse<ArrayList<SearchContact>> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            adapter.refreshContacts(data)
                        }
                    }
                }, { error: Throwable ->
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage ?: "Some unknown error")
                    progressBar.visibility = View.GONE
                })
    }

    override fun onContactSelected(contact: Contact, position: Int) {
        this.dismiss()
        delegate?.onContactSelected(contact)
    }

    interface SearchGoalDelegate {
        fun onContactSelected(contact: Contact)
    }
}