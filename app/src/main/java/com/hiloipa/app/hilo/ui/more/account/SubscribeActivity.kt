package com.hiloipa.app.hilo.ui.more.account

import android.app.Activity
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.vending.billing.IInAppBillingService
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.ChangePaymentRequest
import com.hiloipa.app.hilo.models.requests.SavePlanRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.base.BaseActivity
import com.hiloipa.app.hilo.ui.more.scripts.scriptsHelp
import com.hiloipa.app.hilo.ui.widget.PaymentPlanView
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_subscribe.*
import org.json.JSONException
import org.json.JSONObject

val paymentHelp = "Welcome to the Payments page! Here you can choose the plan you'd like to " +
        "subscribe to or update your billing info."

class SubscribeActivity : BaseActivity(), View.OnClickListener {

    lateinit var plans: MutableList<PaymentPlanView>
    var selectedPlan: PaymentPlanView? = null
    private var billingService: IInAppBillingService? = null
    val REQUEST_CODE = 1001

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            billingService = IInAppBillingService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            billingService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        plans = mutableListOf(silverPlan, goldPlan)
        plans.forEach { it.setOnClickListener(this) }

        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.`package` = "com.android.vending"
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        privacyBtn.setOnClickListener { openUrl("https://www.hiloipa.com/privacy.html") }
        termsBtn.setOnClickListener { openUrl("https://www.hiloipa.com/terms.html") }

        plans.forEach { planView: PaymentPlanView ->
            planView.isChecked = planView.title == HiloApp.userData.userPlan
        }

        makePaymentBtn.setOnClickListener {
            val planView = plans.firstOrNull { it.isChecked }
            if (planView != null)
                updatePaymentPlan(planView)
            else
                Toast.makeText(this, getString(R.string.select_plan_first),
                        Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {
        plans.forEach { it.isChecked = it.id == v.id }
    }

    private fun updatePaymentPlan(planView: PaymentPlanView) {
        if (billingService == null) return

        val buyIntentBundle = billingService!!.getBuyIntent(3, packageName,
                planView.title, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ")

        val pendingIntent: PendingIntent = buyIntentBundle.getParcelable("BUY_INTENT")
        selectedPlan = planView
        startIntentSenderForResult(pendingIntent.intentSender,
                REQUEST_CODE, Intent(), Integer.valueOf(0)!!, Integer.valueOf(0)!!,
                Integer.valueOf(0)!!)
    }

    private fun confirmPayment(planView: PaymentPlanView) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            val responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            val purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            val dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    val jsonObject = JSONObject(purchaseData);
                    val sku = jsonObject.getString("productId");
                    if (selectedPlan != null)
                        confirmPayment(selectedPlan!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    override fun onDestroy() {
        super.onDestroy()
        if (billingService != null) {
            unbindService(serviceConnection)
        }
    }
}
