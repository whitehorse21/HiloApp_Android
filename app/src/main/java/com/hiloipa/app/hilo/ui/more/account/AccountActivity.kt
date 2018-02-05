package com.hiloipa.app.hilo.ui.more.account

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        toolbar.setNavigationOnClickListener { finish() }
        managePaymentPlanBtn.setOnClickListener {
            val intent = Intent(this, SubscribeActivity::class.java)
            startActivity(intent)
        }
    }
}
