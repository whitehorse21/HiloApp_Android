package com.hiloipa.app.hilo.ui.more.notes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.base.BaseActivity
import com.hiloipa.app.hilo.ui.more.scripts.scriptsHelp
import kotlinx.android.synthetic.main.activity_notepad.*

val notepadHelp = "Welcome to the Notepad! Think of this as the bulletin board you’d have up in " +
        "your office or the blotter on your desk. Create notes that you want to keep, or use this " +
        "like sticky notes for tasks that are temporary. Notes can be color-coded and tagged for " +
        "easy reference."
class NotepadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
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
                helpIntent.putExtra(HelpActivity.contentKey, notepadHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }
}
