package com.hiloipa.app.hilo.ui.more.email

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_email_templates.*

val templateHelp = "Welcome to the Email Templates page! From here you can view all of the templates " +
        "you've created on the Hilo desktop site, along with any templates your upline has shared " +
        "with you through Hilo. Please note: you cannot create templates in the app. You can only " +
        "create a new template by logging into the Template Builder page on the main website at " +
        "www.hiloipa.com.\r\n\r\nUse the scroll menu at the top of the page to filter between your " +
        "own personal templates, the ones you've shared, and the ones that have been shared with " +
        "you.\r\n\r\nClick on any template name to preview."

class EmailTemplatesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_templates)
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
                helpIntent.putExtra(HelpActivity.contentKey, templateHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }
}
