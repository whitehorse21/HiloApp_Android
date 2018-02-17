package com.hiloipa.app.hilo.ui.todos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_create_todo.*

class CreateTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        toolbar.setNavigationOnClickListener { finish() }
        val isEvent = intent.extras.getBoolean("is event")

        todoTimeLayout.visibility = if (isEvent) View.GONE else View.VISIBLE
        eventTimeLayout.visibility = if (isEvent) View.VISIBLE else View.GONE
    }
}
