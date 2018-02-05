package com.hiloipa.app.hilo.ui.more.scripts

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ScriptsAdapter
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import kotlinx.android.synthetic.main.activity_scripts.*
import kotlinx.android.synthetic.main.list_item_script.*

class ScriptsActivity : AppCompatActivity(), ScriptsAdapter.ScriptDelegate {

    lateinit var adapter: ScriptsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scripts)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = ScriptsAdapter(this)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        addScriptBtn.setOnClickListener {
            val intent = Intent(this, EditScriptActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onScriptClicked() {
        this.showScriptDialog()
    }

    override fun onEditScriptClicked() {
        val intent = Intent(this, EditScriptActivity::class.java)
        startActivity(intent)
    }

    override fun onDeleteScriptClicked() {

    }

    private fun showScriptDialog() {
        val alertView = layoutInflater.inflate(R.layout.alert_script_details, null)
        val scriptName: RalewayTextView = alertView.findViewById(R.id.scriptNameLabel)
        val script: RalewayTextView = alertView.findViewById(R.id.scriptLabel)
        val editBtn: ImageButton = alertView.findViewById(R.id.editBtn)
        val backBtn: ImageButton = alertView.findViewById(R.id.backButton)
        val copyBtn: RalewayButton = alertView.findViewById(R.id.copyBtn)

        val dialog = AlertDialog.Builder(this)
                .setView(alertView)
                .create()

        backBtn.setOnClickListener { dialog.dismiss() }

        editBtn.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, EditScriptActivity::class.java)
            startActivity(intent)
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
