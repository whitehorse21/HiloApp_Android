package com.hiloipa.app.hilo.ui.auth


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.ResetPassRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.base.BaseFragment
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_restore_password.*


/**
 * A simple [Fragment] subclass.
 */
class RestorePasswordFragment : BaseFragment() {

    companion object {
        fun newInstance(): RestorePasswordFragment {
            val args = Bundle()
            val fragment = RestorePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_restore_password, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { activity!!.onBackPressed() }

        resetPasswordBtn.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val email = emailField.text.toString()
        if (email.isEmpty()) {
            emailField.error = getString(R.string.field_required)
            return
        }

        val dialog = activity!!.showLoading()
        HiloApp.api().resetPassword(ResetPassRequest(email)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        activity!!.showExplanation(title = getString(R.string.success),
                                message = response.message)
                        emailField.setText("")
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
