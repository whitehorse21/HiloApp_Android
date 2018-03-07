package com.hiloipa.app.hilo.ui.more.scripts

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ScriptsAdapter
import com.hiloipa.app.hilo.models.requests.DeleteNoteRequest
import com.hiloipa.app.hilo.models.requests.DeleteScriptRequest
import com.hiloipa.app.hilo.models.requests.ScriptsRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.Script
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.more.email.templateHelp
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scripts.*

val scriptsHelp = "Sometimes getting started is the hardest thing! That’s why we love to provide " +
        "sample language for our team!\r\n\r\nFrom this page you can view and edit scripts you've " +
        "already created on the main website or add new ones by clicking the \"Add Script\" button " +
        "toward the top of the page.\r\n\r\nUse the scroll menu to narrow your list to only see " +
        "your own personal scripts or the ones shared with you by your upline.\r\n\r\nUse these " +
        "scripts in texts by selecting them from the drop down menu when you click the green \"text\" " +
        "icon on the Contact List page, or click on a script's name to preview the script content. " +
        "From there you can \"Copy to Clipboard\" to easily insert the text into a Facebook message " +
        "or email.\r\n\r\nYou can also search for keywords within a script by using the \"Search\" " +
        "field at the top of the page."
class ScriptsActivity : AppCompatActivity(), ScriptsAdapter.ScriptDelegate {

    lateinit var adapter: ScriptsAdapter
    private val allScripts: ArrayList<Script> = arrayListOf()
    private val filteredScripts: ArrayList<Script> = arrayListOf()
    private var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scripts)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = ScriptsAdapter(this)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                getScripts()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        addScriptBtn.setOnClickListener {
            val intent = Intent(this, EditScriptActivity::class.java)
            startActivityForResult(intent, 1201)
        }

        goBtn.setOnClickListener {
            query = searchField.text.toString()
            getScripts(query)
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                query = searchField.text.toString()
                if (query.isEmpty())
                    getScripts()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        getScripts()
    }

    private fun getScripts(query: String = this.query) {
        val loading = showLoading()
        val request = ScriptsRequest()
        request.query = query
        HiloApp.api().getScripts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ArrayList<Script>> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            allScripts.clear()
                            filteredScripts.clear()
                            allScripts.addAll(data)
                            if (tabLayout.selectedTabPosition != 1 && tabLayout.selectedTabPosition != 2)
                                filteredScripts.addAll(allScripts)
                            else if (tabLayout.selectedTabPosition == 1) {
                                allScripts.forEach {
                                    if (it.type == "0")
                                        filteredScripts.add(it)
                                }
                            } else if (tabLayout.selectedTabPosition == 2) {
                                allScripts.forEach {
                                    if (it.type == "1")
                                        filteredScripts.add(it)
                                }
                            }

                            adapter.refreshList(filteredScripts)
                        }
                    } else showExplanation(message = response.message)

                    hideKeyboard()
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    override fun onScriptClicked(script: Script, position: Int) {
        this.showScriptDialog(script)
    }

    override fun onEditScriptClicked(script: Script, position: Int) {
        val intent = Intent(this, EditScriptActivity::class.java)
        intent.putExtra(EditScriptActivity.scriptKey, script)
        startActivityForResult(intent, 1201)
    }

    override fun onDeleteScriptClicked(script: Script, position: Int) {
        val alert = AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete_script))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = showLoading()
                    val request = DeleteScriptRequest()
                    request.scriptId = "${script.id}"
                    HiloApp.api().deleteScript(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    adapter.scripts.remove(script)
                                    adapter.notifyItemRemoved(position)
                                } else showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        alert.show()
    }

    private fun showScriptDialog(script: Script) {
        val alertView = layoutInflater.inflate(R.layout.alert_script_details, null)
        val scriptName: RalewayTextView = alertView.findViewById(R.id.scriptNameLabel)
        val scriptBody: RalewayTextView = alertView.findViewById(R.id.scriptLabel)
        val editBtn: ImageButton = alertView.findViewById(R.id.editBtn)
        val backBtn: ImageButton = alertView.findViewById(R.id.backButton)
        val copyBtn: RalewayButton = alertView.findViewById(R.id.copyBtn)

        scriptName.text = script.title
        scriptBody.text = script.body

        copyBtn.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Script body", script.body)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, getString(R.string.copied_to_clipboard),
                    Toast.LENGTH_SHORT).show()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1201)
            getScripts()
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
                helpIntent.putExtra(HelpActivity.contentKey, scriptsHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }
}
