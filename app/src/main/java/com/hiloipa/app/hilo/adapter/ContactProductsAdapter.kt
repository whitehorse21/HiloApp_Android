package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.ContactProduct
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.squareup.picasso.Picasso

/**
 * Created by eduardalbu on 03.02.2018.
 */
class ContactProductsAdapter(val context: Context): RecyclerView.Adapter<ContactProductsAdapter.ViewHolder>() {

    var delegate: UserProductDelegate? = null
    var products: ArrayList<ContactProduct> = arrayListOf()

    fun refreshProducts(products: ArrayList<ContactProduct>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_user_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val product = products[position]
        holder.product = product
        holder.titleLabel.text = product.name
        Picasso.with(context).load(product.productImage).into(holder.productImage)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val titleLabel: RalewayTextView = itemView.findViewById(R.id.productNameLabel)
        val unasignBtn: RalewayButton = itemView.findViewById(R.id.unasignProductBtn)
        lateinit var product: ContactProduct

        init {
            unasignBtn.setOnClickListener { delegate?.onUnasignProductClicked(product, adapterPosition) }
        }
    }

    interface UserProductDelegate {
        fun onUnasignProductClicked(product: ContactProduct, position: Int)
    }
}