package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.Product
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.squareup.picasso.Picasso

/**
 * Created by eduardalbu on 05.02.2018.
 */
class ProductsAdapter(val context: Context): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    var delegate: ProductDelegate? = null
    val products: ArrayList<Product> = arrayListOf()

    fun refreshProducts(products: ArrayList<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.product = product
        holder.productName.text = product.productName
        holder.wholeSalePrice.text = "${product.wholeSalePrice}"
        holder.pcPrice.text = "${product.piecePrice}"
        holder.retailPrice.text = "${product.retailPrice}"

        Picasso.get().load(product.productImage)
                .into(holder.productImage)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: RalewayTextView = itemView.findViewById(R.id.productNameLabel)
        val wholeSalePrice: RalewayTextView = itemView.findViewById(R.id.wholeSalePriceLabel)
        val pcPrice: RalewayTextView = itemView.findViewById(R.id.pcPriceLabel)
        val retailPrice: RalewayTextView = itemView.findViewById(R.id.retailPriceLabel)
        val assignBtn: RalewayButton = itemView.findViewById(R.id.assignBtn)
        lateinit var product: Product

        init {
            assignBtn.setOnClickListener { delegate?.onProductAssignClicked(product, adapterPosition) }
        }
    }

    interface ProductDelegate {
        fun onProductAssignClicked(product: Product, position: Int)
    }
}