/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : BaseActivity() {

    companion object {
        val titleKey = "com.hiloipa.app.hilo.ui.HELP_TITLE"
        val contentKey = "com.hiloipa.app.hilo.ui.HELP_CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        toolbar.setNavigationOnClickListener { finish() }

        helpText.text = intent.extras.getString(contentKey)
        toolbarTitle.text = getString(R.string.s_help, intent.extras.getString(titleKey))
    }
}
