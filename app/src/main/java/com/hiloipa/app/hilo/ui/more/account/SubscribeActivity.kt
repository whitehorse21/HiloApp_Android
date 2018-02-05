package com.hiloipa.app.hilo.ui.more.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.PaymentPlanView
import com.hiloipa.app.hilo.utils.openUrl
import kotlinx.android.synthetic.main.activity_subscribe.*

class SubscribeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var plans: MutableList<PaymentPlanView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        toolbar.setNavigationOnClickListener { finish() }
        plans = mutableListOf(silverPlan, goldPlan)
        plans.forEach { it.setOnClickListener(this) }

        privacyBtn.setOnClickListener { openUrl("https://www.hiloipa.com/privacy.html") }
        termsBtn.setOnClickListener { openUrl("https://www.hiloipa.com/terms.html") }
    }

    override fun onClick(v: View) {
        plans.forEach { it.isChecked = it.id == v.id }
    }
}
