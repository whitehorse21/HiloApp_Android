package com.hiloipa.app.hilo.ui.more

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.base.BaseActivity
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        toolbar.setNavigationOnClickListener { finish() }

        sendBtn.setOnClickListener {
            hideKeyboard()
            sendFeedback()
        }
    }

    private fun sendFeedback() {
        val comment = feedbackField.text.toString()
        if (comment.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_your_comment),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val loading = showLoading()
        val request = StandardRequest()
        request.comments = Base64.encodeToString(comment.toByteArray(), Base64.DEFAULT)
        HiloApp.api().sendFeedback(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        feedbackField.setText("")
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }
}
