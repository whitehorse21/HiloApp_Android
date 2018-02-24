package com.hiloipa.app.hilo.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.widget.RadioGroup
import com.hiloipa.app.hilo.ui.auth.AuthActivity
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.contacts.ContactsFragment
import com.hiloipa.app.hilo.ui.contacts.DeviceContact
import com.hiloipa.app.hilo.ui.more.MoreFragment
import com.hiloipa.app.hilo.ui.reachout.ReachoutLogsFragment
import com.hiloipa.app.hilo.ui.todos.TodosFragment
import com.hiloipa.app.hilo.ui.tracker.GoalTrackerFragment
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.checkForCrashes
import com.hiloipa.app.hilo.utils.checkForUpdates
import com.hiloipa.app.hilo.utils.unregisterManagers
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    private var selectedTab: SelectedTab = SelectedTab.tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnCheckedChangeListener(this)
        if (!HiloApp.instance.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        } else
            replaceFragment(GoalTrackerFragment.newInstance(), title = getString(R.string.goal_tracker))

        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        checkForCrashes()
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false, title: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.name)
        transaction.commit()
        toolbarTitle.text = title
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.navGoalTracker -> {
                if (selectedTab != SelectedTab.tracker) {
                    replaceFragment(GoalTrackerFragment.newInstance(), title = getString(R.string.goal_tracker))
                    selectedTab = SelectedTab.tracker
                }
            }
            R.id.navContacts -> {
                if (selectedTab != SelectedTab.contacts) {
                    replaceFragment(ContactsFragment.newInstance(), title = getString(R.string.contacts))
                    selectedTab = SelectedTab.contacts
                }
            }

            R.id.navReachoutLogs -> {
                if (selectedTab != SelectedTab.logs) {
                    replaceFragment(ReachoutLogsFragment.newInstance(), title = getString(R.string.reachout_log))
                    selectedTab = SelectedTab.logs
                }
            }

            R.id.navToDos -> {
                if (selectedTab != SelectedTab.todos) {
                    replaceFragment(TodosFragment.newInstance(), title = getString(R.string.todos))
                    selectedTab = SelectedTab.todos
                }
            }

            R.id.navMore -> {
                if (selectedTab != SelectedTab.more) {
                    replaceFragment(MoreFragment.newInstance(), title = getString(R.string.more))
                    selectedTab = SelectedTab.more
                }
            }
        }
    }

    enum class SelectedTab {
        tracker, contacts, logs, todos, more
    }

    fun getDeviceContacts(): Observable<ArrayList<DeviceContact>> = Observable.create { subscriber ->
        val deviceContacts = arrayListOf<DeviceContact>()
        val resolver = contentResolver
        val contacts = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        if (contacts != null && contacts.moveToFirst()) {
            while (contacts.moveToNext()) {
                val contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhone = contacts.getInt(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                // get contact email address
                var email = ""
                val emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(contactId), null)
                if (emails != null && emails.moveToFirst()) {
                    email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    emails.close()
                }

                // get contact phone number
                var phone = ""
                if (hasPhone > 0) {
                    val numbers = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(contactId), null)
                    if (numbers != null && numbers.moveToFirst()) {
                        phone = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        numbers.close()
                    }
                }

                // create device contact object
                val deviceContact = DeviceContact()
                deviceContact.email = email
                deviceContact.number = phone
                val names = contactName.split(" ")
                if (names.size > 0)
                    deviceContact.firstName = names[0]
                else deviceContact.firstName = ""

                if (names.size > 1)
                    deviceContact.lastName = names[1]
                else deviceContact.lastName = ""

                deviceContacts.add(deviceContact)
            }
        }
        contacts.close()
        subscriber.onNext(deviceContacts)
        subscriber.onComplete()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
    }
}
