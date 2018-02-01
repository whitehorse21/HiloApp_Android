package com.hiloipa.app.hilo.ui.reachout


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ReachoutLogsAdapter
import kotlinx.android.synthetic.main.fragment_rachout_logs.*


/**
 * A simple [Fragment] subclass.
 */
class ReachoutLogsFragment : Fragment() {

    lateinit var adapter: ReachoutLogsAdapter

    companion object {
        fun newInstance(): ReachoutLogsFragment {
            val args = Bundle()
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
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
    }
}
