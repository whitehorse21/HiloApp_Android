package com.hiloipa.app.hilo.ui.reachout


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ReachoutLogsAdapter
import com.hiloipa.app.hilo.ui.contacts.ContactDetailsActivity
import kotlinx.android.synthetic.main.fragment_rachout_logs.*


/**
 * A simple [Fragment] subclass.
 */
class ReachoutLogsFragment : Fragment(), ReachoutLogsAdapter.ReachOutDelegate {

    lateinit var adapter: ReachoutLogsAdapter

    companion object {
        fun newInstance(isChild: Boolean = false): ReachoutLogsFragment {
            val args = Bundle()
            args.putBoolean("isChild", isChild)
            val fragment = ReachoutLogsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_rachout_logs, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReachoutLogsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false

        addReachOutBtn.setOnClickListener {
            val intent = Intent(activity, EditReachoutActivity::class.java)
            activity.startActivity(intent)
        }

        val isChild = arguments.getBoolean("isChild")
        if (isChild) {
            searchLayout.visibility = View.GONE
            searchBottomLine.visibility = View.GONE
            resetBtn.visibility = View.GONE
        }
    }

    override fun onContactNameClicked() {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onEditLogClicked() {
        val intent = Intent(activity, EditReachoutActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onDeleteLogClicked() {

    }
}
