package com.hiloipa.app.hilo.ui.more.products

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_products.*

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        toolbar.setNavigationOnClickListener { finish() }
    }
}
