package com.hiloipa.app.hilo


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
}
