package com.hilocrm.app.hilo.ui.more.scripts

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.requests.SaveScriptRequest
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.models.responses.Script
import com.hilocrm.app.hilo.ui.base.BaseActivity
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_script.*

class EditScriptActivity : BaseActivity() {

    var script: Script? = null

    companion object {
        const val scriptKey = "com.hiloipa.app.hilo.ui.more.scripts.SCRIPT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_script)
        toolbar.setNavigationOnClickListener { finish() }
        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        if (intent.extras != null && intent.extras.containsKey(scriptKey)) {
            script = intent.extras.getParcelable(scriptKey)
            scriptNameField.setText(script!!.title)
            scriptContentField.setText(script!!.body)
            val checkedId = if (script!!.type == "0")
                R.id.personalBtn else R.id.sharedWithDownlineBtn
            scriptTypeGroup.check(checkedId)
        } else {
            cardTitle.text = getString(R.string.add_script)
            toolbarTitle.text = getString(R.string.add_script)
        }

        saveBtn.setOnClickListener { saveScript() }
    }

    private fun saveScript() {
        val title = scriptNameField.text.toString()
        val content = scriptContentField.text.toString()

        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_script_title),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (content.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_script_content),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val loading = showLoading()
        val request = SaveScriptRequest()
        request.title = title
        request.content = content
        request.scriptId = script?.id?.toString()
        request.type = if (personalBtn.isChecked) "0" else "1"

        val observable: Observable<HiloResponse<String>>
        if (script != null)
            observable = HiloApp.api().updateScript(request)
        else
            observable = HiloApp.api().addScript(request)

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }
}
