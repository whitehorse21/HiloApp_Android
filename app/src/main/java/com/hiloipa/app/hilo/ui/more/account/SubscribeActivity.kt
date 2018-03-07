package com.hiloipa.app.hilo.ui.more.account

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.ChangePaymentRequest
import com.hiloipa.app.hilo.models.requests.SavePlanRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.more.scripts.scriptsHelp
import com.hiloipa.app.hilo.ui.widget.PaymentPlanView
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_subscribe.*

val paymentHelp = "Welcome to the Payments page! Here you can choose the plan you'd like to " +
        "subscribe to or update your billing info."
class SubscribeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var plans: MutableList<PaymentPlanView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        plans = mutableListOf(silverPlan, goldPlan)
        plans.forEach { it.setOnClickListener(this) }

        privacyBtn.setOnClickListener { openUrl("https://www.hiloipa.com/privacy.html") }
        termsBtn.setOnClickListener { openUrl("https://www.hiloipa.com/terms.html") }

        plans.forEach { planView: PaymentPlanView ->
            planView.isChecked = planView.title == HiloApp.userData.userPlan
        }

        makePaymentBtn.setOnClickListener {
            val planView = plans.first { it.isChecked }
            updatePaymentPlan(planView)
        }
    }

    override fun onClick(v: View) {
        plans.forEach { it.isChecked = it.id == v.id }
    }

    private fun updatePaymentPlan(planView: PaymentPlanView) {
        val loading = showLoading()
        val request = ChangePaymentRequest()
        request.planId = planView.tag as String
        request.price = if (planView.tag == "Silver") "19.99" else "39.99"

        HiloApp.api().changePaymentPlan(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        HiloApp.userData.userPlan = planView.title
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
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
                helpIntent.putExtra(HelpActivity.contentKey, paymentHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }
}
