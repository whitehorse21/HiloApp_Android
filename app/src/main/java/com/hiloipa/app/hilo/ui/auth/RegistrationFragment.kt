package com.hiloipa.app.hilo.ui.auth


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.fragment_registration.*


/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {

    lateinit var authActivity: AuthActivity

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_registration, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { activity.onBackPressed() }

        termsAndCoditionsBtn.setOnClickListener { authActivity.replaceFragment(TermsFragment.newInstance()) }

        signUpBtn.setOnClickListener {
            val dialogView = activity.layoutInflater.inflate(R.layout.alert_beta_testing, null)
            val gotItBtn: Button = dialogView.findViewById(R.id.gotItBtn)
            val dialog = AlertDialog.Builder(activity)
                    .setView(dialogView).create()
            gotItBtn.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
    }
}
