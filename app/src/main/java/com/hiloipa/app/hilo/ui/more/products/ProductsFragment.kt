package com.hiloipa.app.hilo.ui.more.products


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ProductsAdapter
import com.hiloipa.app.hilo.models.requests.AssignProductRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.base.BaseFragment
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_products.*


/**
 * A simple [Fragment] subclass.
 */
class ProductsFragment : BaseFragment(), ProductsAdapter.ProductDelegate, TextWatcher {

    lateinit var adapter: ProductsAdapter
    private val filteredProducts: ArrayList<Product> = arrayListOf()
    private val allProducts: ArrayList<Product> = arrayListOf()

    companion object {
        fun newInstance(): ProductsFragment {
            val args = Bundle()
            val fragment = ProductsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_products, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductsAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false

        searchField.addTextChangedListener(this)

        getProducts()
    }

    private fun getProducts() {
        val loading = activity!!.showLoading()
        HiloApp.api().getAllProducts(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<AllProductsResponse> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            allProducts.clear()
                            allProducts.addAll(data.products)
                            if (data.products.isEmpty())
                                adapter.refreshProducts(data.userProducts)
                            else
                                adapter.refreshProducts(data.products)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onProductAssignClicked(product: Product, position: Int) {
        val loading = activity!!.showLoading()
        HiloApp.api().getProductContacts(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<RevealProductsContacts> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            showAssignDialog(product, data.contacts)
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        filteredProducts.clear()
        val query = searchField.text.toString()
        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts)
        } else {
            allProducts.forEach {
                if (it.productName.contains(query, true))
                    filteredProducts.add(it)
            }
        }

        adapter.refreshProducts(filteredProducts)
    }

    private fun showAssignDialog(product: Product, contacts: ArrayList<DashContact>) {
        val assignAsValues = arrayListOf("", "Product purchased", "Recommended product",
                "Product Interested", "Solution tool recommended")
        val alertView = layoutInflater.inflate(R.layout.alert_assign_product, null)
        val asignAsBtn: RalewayButton = alertView.findViewById(R.id.assignAsBtn)
        val asignAsSpinner: Spinner = alertView.findViewById(R.id.assignAsSpinner)
        val asignToBtn: RalewayButton = alertView.findViewById(R.id.assignToBtn)
        val asignToSpinner: Spinner = alertView.findViewById(R.id.assignToSpinner)
        val submitBtn: RalewayButton = alertView.findViewById(R.id.submitBtn)

        val dialog = AlertDialog.Builder(activity!!)
                .setView(alertView)
                .create()

        asignAsSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, assignAsValues)
        asignAsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    asignAsBtn.text = assignAsValues[position]
                    asignAsBtn.tag = assignAsValues[position]
                } else {
                    asignAsBtn.text = ""
                    asignAsBtn.tag = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        asignAsBtn.setOnClickListener {
            asignAsSpinner.performClick()
        }

        val contactNames = arrayListOf("")
        contacts.forEach { contactNames.add(it.contactName) }
        asignToSpinner.adapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, contactNames)
        asignToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val contact = contacts[position - 1]
                    asignToBtn.text = contact.contactName
                    asignToBtn.tag = "${contact.contactId}"
                } else {
                    asignToBtn.text = ""
                    asignToBtn.tag = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        asignToBtn.setOnClickListener {
            asignToSpinner.performClick()
        }

        submitBtn.setOnClickListener {
            val productType = asignAsBtn.text.toString()
            if (productType.isEmpty()) {
                Toast.makeText(activity, getString(R.string.select_product_type),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contactId = asignToBtn.tag as String? ?: ""
            if (contactId.isEmpty()) {
                Toast.makeText(activity, getString(R.string.select_product_contact),
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = AssignProductRequest()
            request.productId = "${product.productId}"
            request.productType = productType
            request.contactId = contactId

            dialog.dismiss()
            val loading = activity!!.showLoading()
            HiloApp.api().assignProduct(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.status.isSuccess()) {
                            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                        } else activity!!.showExplanation(message = response.message)
                    }, { error: Throwable ->
                        loading.dismiss()
                        error.printStackTrace()
                        activity!!.showExplanation(message = error.localizedMessage)
                    })
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
