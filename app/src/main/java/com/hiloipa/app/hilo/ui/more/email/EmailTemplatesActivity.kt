package com.hiloipa.app.hilo.ui.more.email

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_email_templates.*

class EmailTemplatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_templates)
        toolbar.setNavigationOnClickListener { finish() }
    }
}
