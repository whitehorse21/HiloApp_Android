package com.hiloipa.app.hilo


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var authActivity: AuthActivity

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountBtn.setOnClickListener {
            authActivity.replaceFragment(RegistrationFragment.newInstance())
        }

        forgotPasswordBtn.setOnClickListener {
            authActivity.replaceFragment(RestorePasswordFragment.newInstance())
        }
    }
}
