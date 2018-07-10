package com.hilocrm.app.hilo.ui.auth


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.models.requests.RegistrationRequest
import com.hilocrm.app.hilo.models.responses.*
import com.hilocrm.app.hilo.ui.MainActivity
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.ui.widget.RalewayTextView
import com.hilocrm.app.hilo.utils.*
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_registration.*


/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : BaseFragment() {

    private lateinit var authActivity: AuthActivity
    lateinit var signUpData: SignUpResponse
    var selectedCompanyName: CompanyName? = null
    var selectedFindSounce: HearName? = null

    companion object {
        fun newInstance(): RegistrationFragment {
            val args = Bundle()
            val fragment = RegistrationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authActivity = context as AuthActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_registration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { activity!!.onBackPressed() }

        termsAndCoditionsBtn.setOnClickListener { authActivity.replaceFragment(TermsFragment.newInstance()) }

        this.prepareSignUpData()

        signUpBtn.setOnClickListener { createNewAccount() }

        companyField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                companySpinner.performClick()
                companyField.clearFocus()
            }
        }

        findUsField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                findUsSpinner.performClick()
                findUsField.clearFocus()
            }
        }
    }

    private fun createNewAccount() {
        if (!termsAndCoditionsCheckBox.isChecked) {
            Toast.makeText(activity!!, getString(R.string.accept_tersms), Toast.LENGTH_SHORT).show()
            return
        }

        val firstName = firstNameField.text.toString()
        val lastName = lastNameField.text.toString()
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val repeatPass = repeatPasswordField.text.toString()
        val screenSize = activity!!.displaySize()
        val screenSizeText = "${screenSize.first}*${screenSize.second}"

        if (firstName.isEmpty()) {
            firstNameField.error = getString(R.string.field_required)
            return
        }

        if (lastName.isEmpty()) {
            lastNameField.error = getString(R.string.field_required)
            return
        }

        if (email.isEmpty()) {
            emailField.error = getString(R.string.field_required)
            return
        }

        if (password.isEmpty()) {
            passwordField.error = getString(R.string.field_required)
            return
        }

        if (repeatPass.isEmpty() || repeatPass != password) {
            firstNameField.error = getString(R.string.passwords_do_not_match)
            return
        }

        if (selectedCompanyName == null) {
            Toast.makeText(activity!!, getString(R.string.select_company), Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedFindSounce == null) {
            Toast.makeText(activity!!, getString(R.string.how_did_you_hear_about_us), Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = activity!!.showLoading()

        val registrationRequest = RegistrationRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                company_Name = selectedCompanyName!!.companyName,
                hear = selectedFindSounce!!.hearId,
                company_Id = selectedCompanyName!!.companyId,
                screenSize = screenSizeText
        )

        HiloApp.api().createNewAccount(registrationRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<UserData> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            HiloApp.userData = data
                            HiloApp.instance.saveAccessToken(data.accessToken)
                            HiloApp.instance.setIsLoggedIn(true)
                            HiloApp.instance.saveUserCredentials(registrationRequest.email,
                                    registrationRequest.password)
                            showWelcomeDialog(data.messageBody, data.messageImage, data.messageTitle)
                        } else
                            activity!!.showExplanation(message = getString(R.string.unknown_error))
                    } else
                        activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun prepareSignUpData() {
        val dialog = activity!!.showLoading()
        HiloApp.api().getSignUpData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<SignUpResponse> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            this.prepareUiWithNewData(data)
                        else activity!!.onBackPressed()
                    } else {
                        activity!!.showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    private fun prepareUiWithNewData(signUpData: SignUpResponse) {
        this.signUpData = signUpData
        // setup companies spinner
        val companyNames = mutableListOf<String>()
        companyNames.add(getString(R.string.select_company))
        signUpData.companies.forEach { companyNames.add(it.companyName) }
        companySpinner.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_dropdown_item, companyNames)
        companySpinner.onItemSelectedListener = onCompanySelectedListener

        // setup find us spinner
        val findSources = mutableListOf<String>()
        findSources.add(getString(R.string.how_did_you_hear_about_us))
        signUpData.hears.forEach { findSources.add(it.hearName) }
        findUsSpinner.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_dropdown_item, findSources)
        findUsSpinner.onItemSelectedListener = onFindUsSelectedListener
    }

    private val onCompanySelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position != 0) {
                selectedCompanyName = signUpData.companies[position - 1]
                companyField.setText(selectedCompanyName!!.companyName)
            } else {
                companyField.setText("")
                selectedCompanyName = null
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    private val onFindUsSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position != 0) {
                selectedFindSounce = signUpData.hears[position - 1]
                findUsField.setText(selectedFindSounce!!.hearName)
            } else {
                findUsField.setText("")
                selectedFindSounce = null
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    private fun showWelcomeDialog(message: String?, messageImage: String?, messageTitle: String?) {
        if (message != null && messageTitle != null) {
            val dialogView = activity!!.layoutInflater.inflate(R.layout.alert_beta_testing, null)
            val gotItBtn: Button = dialogView.findViewById(R.id.gotItBtn)
            val titleLabel: RalewayTextView = dialogView.findViewById(R.id.title)
            val messageLabel: RalewayTextView = dialogView.findViewById(R.id.messageLabel)
            val imageView: ImageView = dialogView.findViewById(R.id.betaImage)

            Picasso.get().load(messageImage).into(imageView)
            titleLabel.text = messageTitle
            messageLabel.text = message

            val dialog = AlertDialog.Builder(activity!!)
                    .setView(dialogView).create()

            gotItBtn.setOnClickListener {
                dialog.dismiss()
                val feedbackIntent = Intent(activity!!, MainActivity::class.java)
                activity!!.startActivity(feedbackIntent)
                activity!!.finish()
            }
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        } else {
            val feedbackIntent = Intent(activity!!, MainActivity::class.java)
            activity!!.startActivity(feedbackIntent)
            activity!!.finish()
        }
    }
}
