package com.hilocrm.app.hilo.ui.reachout


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.adapter.ReachoutLogsAdapter
import com.hilocrm.app.hilo.models.requests.DeleteReachOutRequest
import com.hilocrm.app.hilo.models.requests.ReachOutLogsRequest
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.models.responses.ReachOutLog
import com.hilocrm.app.hilo.models.responses.ReachOutLogs
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.ui.contacts.details.ContactDetailsActivity
import com.hilocrm.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rachout_logs.*


/**
 * A simple [Fragment] subclass.
 */
class ReachoutLogsFragment : BaseFragment(), ReachoutLogsAdapter.ReachOutDelegate {

    lateinit var adapter: ReachoutLogsAdapter
    var page: Int = 1
    var search: String? = null
    var contactId: String? = null

    companion object {
        fun newInstance(contactId: String? = null): ReachoutLogsFragment {
            val args = Bundle()
            if (contactId != null)
                args.putString("contactId", contactId)
            val fragment = ReachoutLogsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_rachout_logs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReachoutLogsAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false

        // setup reset button
        resetBtn.setOnClickListener {
            page = 1
            search = null
            searchField.setText("")
            searchField.clearFocus()
            activity!!.hideKeyboard()
            getReachOutLogs()
        }

        contactId = arguments!!.getString("contactId", null)
        // if we have a contact id we need to hide top views
        // because fragment was added in ContactDetailsActivity
        if (contactId != null) {
            searchLayout.visibility = View.GONE
            searchBottomLine.visibility = View.GONE
            resetBtn.visibility = View.GONE
        }

        // setup add reachout button
        addReachOutBtn.setOnClickListener {
            val intent = Intent(activity, EditReachoutActivity::class.java)
            if (contactId != null)
                intent.putExtra("contactId", contactId!!)
            activity!!.startActivityForResult(intent, 1022)
        }

        // setup search button
        goBtn.setOnClickListener {
            val searchQuery = searchField.text.toString()
            if (searchQuery.isEmpty()) search = null
            else search = searchQuery
            page = 1
            getReachOutLogs()
        }

        // setup load more button
        loadMoreBtn.setOnClickListener {
            page++
            getReachOutLogs(refresh = false)
        }

        // setup search field so we can refresh list wan it's text is empty
        searchField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = searchField.text.toString()
                if (newText.isEmpty()) {
                    page = 1
                    search = null
                    getReachOutLogs()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        getReachOutLogs()
    }

    private fun getReachOutLogs(page: Int = this.page, search: String? = this.search,
                                contactId: String? = this.contactId, refresh: Boolean = true) {
        val request = ReachOutLogsRequest()
        request.page = page
        request.search = search
        request.contactId = contactId

        val loading = activity!!.showLoading()
        HiloApp.api().getReachOutLogs(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ReachOutLogs> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            // hide load more btn if we've reached maximum pages
                            if (data.currentPage == data.totalPages)
                                loadMoreBtn.visibility = View.GONE

                            // set data in adapter
                            if (refresh)
                                adapter.refreshLogList(data.reachOutLogs)
                            else
                                adapter.addLogs(data.reachOutLogs)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onContactNameClicked(log: ReachOutLog, position: Int) {
        if (contactId == null) {
            val intent = Intent(activity, ContactDetailsActivity::class.java)
            val extras = Bundle()
            extras.putString(ContactDetailsActivity.contactIdKey, "${log.contactId}")
            intent.putExtras(extras)
            activity!!.startActivity(intent)
        }
    }

    override fun onEditLogClicked(log: ReachOutLog, position: Int) {
        val intent = Intent(activity, EditReachoutActivity::class.java)
        val extras = Bundle()
        extras.putParcelable(EditReachoutActivity.logKey, log)
        intent.putExtras(extras)
        if (contactId != null)
            intent.putExtra("contactId", contactId!!)
        activity!!.startActivityForResult(intent, 1022)
    }

    override fun onDeleteLogClicked(log: ReachOutLog, position: Int) {
        val dialog = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete_log, log.contactName))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity!!.showLoading()
                    val request = DeleteReachOutRequest()
                    request.logId = "${log.historyID}"
                    HiloApp.api().deleteReachOutLog(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    adapter.deleteLog(log)
                                } else activity!!.showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity!!.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1022 && resultCode == Activity.RESULT_OK) {
            page = 1
            getReachOutLogs()
        }
    }
}
