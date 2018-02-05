package com.hiloipa.app.hilo.ui.more.products


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ProductsAdapter
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import kotlinx.android.synthetic.main.fragment_products.*


/**
 * A simple [Fragment] subclass.
 */
class ProductsFragment : Fragment(), ProductsAdapter.ProductDelegate {

    lateinit var adapter: ProductsAdapter

    companion object {
        fun newInstance(): ProductsFragment {
            val args = Bundle()
            val fragment = ProductsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_products, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
    }

    override fun onProductAssignClicked() {
        this.showAssignDialog()
    }

    private fun showAssignDialog() {
        val alertView = layoutInflater.inflate(R.layout.alert_assign_product, null)
        val asignAsBtn: RalewayButton = alertView.findViewById(R.id.assignAsBtn)
        val asignAsSpinner: Spinner = alertView.findViewById(R.id.assignAsSpinner)
        val asignToBtn: RalewayButton = alertView.findViewById(R.id.assignToBtn)
        val asignToSpinner: Spinner = alertView.findViewById(R.id.assignToSpinner)
        val submitBtn: RalewayButton = alertView.findViewById(R.id.submitBtn)

        val dialog = AlertDialog.Builder(activity)
                .setView(alertView)
                .create()

        submitBtn.setOnClickListener { dialog.dismiss() }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
