package com.hilocrm.app.hilo.ui.more.products

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.ui.HelpActivity
import com.hilocrm.app.hilo.ui.base.BaseActivity
import com.hilocrm.app.hilo.ui.more.scripts.scriptsHelp
import kotlinx.android.synthetic.main.activity_products.*

val productsHelp = "Here we list all of your company’s products. Click \"Assign Product\" to log " +
        "this product as one that a contact has purchased, shown interest in, or has been " +
        "recommended. Once assigned, you'll be able to see this product in the \"Products\" " +
        "section of a contact's detail information."
class ProductsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_help -> {
                val helpIntent = Intent(this, HelpActivity::class.java)
                helpIntent.putExtra(HelpActivity.titleKey, toolbarTitle.text)
                helpIntent.putExtra(HelpActivity.contentKey, productsHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }
}
