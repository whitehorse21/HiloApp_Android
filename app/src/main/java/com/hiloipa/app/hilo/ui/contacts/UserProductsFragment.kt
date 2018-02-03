package com.hiloipa.app.hilo.ui.contacts


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.fragment_user_products.*


/**
 * A simple [Fragment] subclass.
 */
class UserProductsFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstace(): UserProductsFragment {
            val args = Bundle()
            val fragment = UserProductsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_user_products, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        purchasedProductsBtn.setOnClickListener(this)
        recomendedProductsBtn.setOnClickListener(this)
        interestedProductsBtn.setOnClickListener(this)
        toolRecomendedProductsBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        UserProductsListFragment.newInstace().show(childFragmentManager, "ProductsListFragment")
    }
}
