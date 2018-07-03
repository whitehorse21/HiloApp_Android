package com.hiloipa.app.hilo.ui.auth

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.base.BaseActivity

class AuthActivity : BaseActivity() {

    lateinit var loginFragment: LoginFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        this.loginFragment = LoginFragment.newInstance()

        replaceFragment(loginFragment, addToBackStack = false)
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.name)
        transaction.commit()
    }
}
