package com.hiloipa.app.hilo.ui.more.notes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_notepad.*

class NotepadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }
}
