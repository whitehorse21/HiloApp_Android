package com.hiloipa.app.hilo.ui.more.scripts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_edit_script.*

class EditScriptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_script)
        toolbar.setNavigationOnClickListener { finish() }
        cancelBtn.setOnClickListener { finish() }
    }
}
