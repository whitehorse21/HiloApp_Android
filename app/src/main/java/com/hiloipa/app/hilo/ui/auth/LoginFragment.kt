package com.hiloipa.app.hilo.ui.auth


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
import android.widget.Button
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.LoginRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.UserData
import com.hiloipa.app.hilo.ui.MainActivity
import com.hiloipa.app.hilo.ui.base.BaseFragment
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.*
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {

    lateinit var authActivity: AuthActivity
    lateinit var response: HiloResponse<UserData>

    companion object {
        fun newInstance(): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
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
        inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountBtn.setOnClickListener {
            authActivity.replaceFragment(RegistrationFragment.newInstance())
        }

        forgotPasswordBtn.setOnClickListener {
            authActivity.replaceFragment(RestorePasswordFragment.newInstance())
        }

        loginBtn.setOnClickListener {
            val dialog = activity!!.showLoading();
            val screenSize = activity!!.displaySize()
            val screenSizeText = "${screenSize.first}*${screenSize.second}"
            val loginRequest = LoginRequest(email = emailField.text.toString(),
                    password = passwordField.text.toString(), screenSize = screenSizeText)
            HiloApp.api().login(loginRequest).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<UserData> ->
                        dialog.dismiss()
                        this.response = response
                        val data = response.data
                        if (data != null && response.status!!.isSuccess()) {
                            HiloApp.instance.saveAccessToken(data.accessToken)
                            HiloApp.instance.setIsLoggedIn(true)
                            HiloApp.userData = data
                            HiloApp.instance.saveUserCredentials(loginRequest.email, loginRequest.password)
                            showWelcomeDialog(data.messageBody, data.messageImage, data.messageTitle)
                        } else {
                            activity!!.showExplanation(message = response.message)
                        }
                    }, { error: Throwable ->
                        dialog.dismiss()
                        error.printStackTrace()
                        activity!!.showExplanation(message = error.localizedMessage)
                    })
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
                val feedbackIntent = Intent(activity, MainActivity::class.java)
                activity!!.startActivity(feedbackIntent)
                activity!!.finish()
            }
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        } else {
            val feedbackIntent = Intent(activity, MainActivity::class.java)
            activity!!.startActivity(feedbackIntent)
            activity!!.finish()
        }
    }
}
