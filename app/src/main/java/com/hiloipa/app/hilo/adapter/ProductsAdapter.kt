package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 05.02.2018.
 */
class ProductsAdapter(val context: Context): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    var delegate: ProductDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: RalewayTextView = itemView.findViewById(R.id.productNameLabel)
        val wholeSalePrice: RalewayTextView = itemView.findViewById(R.id.wholeSalePriceLabel)
        val pcPrice: RalewayTextView = itemView.findViewById(R.id.pcPriceLabel)
        val retailPrice: RalewayTextView = itemView.findViewById(R.id.retailPriceLabel)
        val assignBtn: RalewayButton = itemView.findViewById(R.id.assignBtn)

        init {
            assignBtn.setOnClickListener { delegate?.onProductAssignClicked() }
        }
    }

    interface ProductDelegate {
        fun onProductAssignClicked()
    }
}