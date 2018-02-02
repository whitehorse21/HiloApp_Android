package com.hiloipa.app.hilo.ui.contacts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.activity_edit_contact.*

class EditContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        toolbar.setNavigationOnClickListener { finish() }
        replaceFragment(EditContactFragment.newInstance())
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        transaction.commit()
    }
}
