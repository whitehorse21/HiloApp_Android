package com.hilocrm.app.hilo.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.requests.LoginRequest
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.models.responses.UserData
import com.hilocrm.app.hilo.ui.auth.AuthActivity
import com.hilocrm.app.hilo.ui.base.BaseActivity
import com.hilocrm.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (HiloApp.instance.isLoggedIn()) {
            this.authenticate()
        } else {
            val feedbackIntent = Intent(this, AuthActivity::class.java)
            startActivity(feedbackIntent)
            finish()
        }
    }

    private fun authenticate() {
        val screenSize = displaySize()
        val screenSizeText = "${screenSize.first}*${screenSize.second}"
        val loginRequest = LoginRequest(email = HiloApp.instance.getUsername(),
                password = HiloApp.instance.getPassword(), screenSize = screenSizeText)
        HiloApp.api().login(loginRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<UserData> ->
                    val data = response.data
                    if (data != null && response.status.isSuccess()) {
                        HiloApp.instance.saveAccessToken(data.accessToken)
                        HiloApp.instance.setIsLoggedIn(true)
                        HiloApp.userData = data
                        HiloApp.instance.saveUserCredentials(loginRequest.email, loginRequest.password)
                        val feedbackIntent = Intent(this, MainActivity::class.java)
                        startActivity(feedbackIntent)
                        finish()
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        val feedbackIntent = Intent(this, AuthActivity::class.java)
                        startActivity(feedbackIntent)
                        finish()
                    }
                }, { error: Throwable ->
                    error.printStackTrace()
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    val feedbackIntent = Intent(this, AuthActivity::class.java)
                    startActivity(feedbackIntent)
                    finish()
                })
    }
}
