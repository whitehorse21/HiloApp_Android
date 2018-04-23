package com.hiloipa.app.hilo.ui.more.account

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.UpdateAccountRequest
import com.hiloipa.app.hilo.models.responses.Account
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        managePaymentPlanBtn.setOnClickListener {
            val intent = Intent(this, SubscribeActivity::class.java)
            startActivity(intent)
        }

        updateAccountBtn.setOnClickListener {
            updateAccountDetails()
        }

        getAccountData()
    }

    private fun getAccountData() {
        val loading = showLoading()
        HiloApp.api().getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<Account> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.account = data
                            this.updateUI()
                        }
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateUI(account: Account = this.account) {
        firstNameField.setText(account.firstName)
        lastNameField.setText(account.lastName)
        emailField.setText(account.email)
        hiloEmailField.setText(account.bccEmail)
        phoneNumberField.setText(account.phoneNumber)
        personalUrlField.setText(account.personalUrl)
        businessUrlField.setText(account.businessSite)
        solutionToolUrlField.setText(account.solutionToolLink)
        titleField.setText(account.title)
        companyField.setText(account.company)
    }

    private fun updateAccountDetails() {
        val firstName = firstNameField.text.toString()
        val lastName = lastNameField.text.toString()
        val email = emailField.text.toString()
        val bccEmail = hiloEmailField.text.toString()
        val phoneNumber = phoneNumberField.text.toString()
        val personalUrl = personalUrlField.text.toString()
        val businessUrl = businessUrlField.text.toString()
        val solutionToolLink = solutionToolUrlField.text.toString()
        val title = titleField.text.toString()
        val company = companyField.text.toString()

        if (firstName.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.first_name)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.last_name)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.email)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (bccEmail.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.hilo_bcc_email)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.phone_number)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.WEB_URL.matcher(personalUrl).matches()) {
            personalUrlField.error = getString(R.string.invalid_url)
            return
        }

        if (!Patterns.WEB_URL.matcher(businessUrl).matches()) {
            businessUrlField.error = getString(R.string.invalid_url)
            return
        }

        if (!Patterns.WEB_URL.matcher(solutionToolLink).matches()) {
            solutionToolUrlField.error = getString(R.string.invalid_url)
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.title)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (company.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s, getString(R.string.company)),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateAccountRequest()
        request.firstName = firstName
        request.lastName = lastName
        request.email = email
        request.bccEmail = bccEmail
        request.phoneNumber = phoneNumber
        request.personalUrl = personalUrl
        request.businessSite = businessUrl
        request.solutionToolLink = solutionToolLink
        request.title = title
        request.company = company

        val loading = showLoading()
        HiloApp.api().updateAccount(request)
                .subscribeOn(Schedulers.io())
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

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }
}
