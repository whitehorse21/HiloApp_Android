package com.hiloipa.app.hilo.ui.contacts

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactProductsAdapter
import kotlinx.android.synthetic.main.fragment_user_products_list.*

/**
 * Created by eduardalbu on 03.02.2018.
 */
class UserProductsListFragment: BottomSheetDialogFragment(), ContactProductsAdapter.UserProductDelegate {

    lateinit var adapter: ContactProductsAdapter

    companion object {
        fun newInstace(): UserProductsListFragment {
            val args = Bundle()
            val fragment = UserProductsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater?.inflate(R.layout.fragment_user_products_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { this.dismiss() }
        adapter = ContactProductsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onUnasignProductClicked() {

    }
}