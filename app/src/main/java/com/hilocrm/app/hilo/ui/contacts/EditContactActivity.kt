package com.hilocrm.app.hilo.ui.contacts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_contact.*

class EditContactActivity : BaseActivity() {

    companion object {
        const val contactIdKey = "com.hiloipa.app.hilo.ui.contacts.CONTACT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        toolbar.setNavigationOnClickListener { finish() }
        if (intent.extras == null || !intent.extras.containsKey(contactIdKey)) {
            toolbarTitle.text = getString(R.string.add_contact)
        }
        val contactId: String? = intent.extras?.getString(contactIdKey, null)
        replaceFragment(EditContactFragment.newInstance(contactId))
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        transaction.commit()
    }
}
