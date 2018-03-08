package com.hiloipa.app.hilo.ui

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
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
import kotlin.coroutines.experimental.coroutineContext

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    private var selectedTab: SelectedTab = SelectedTab.tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_help -> {
                val helpIntent = Intent(this, HelpActivity::class.java)
                helpIntent.putExtra(HelpActivity.titleKey, selectedTab.title())
                helpIntent.putExtra(HelpActivity.contentKey, selectedTab.helpText())
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.navGoalTracker -> {
                if (selectedTab != SelectedTab.tracker) {
                    toolbar.menu.findItem(R.id.action_help).isVisible = true
                    replaceFragment(GoalTrackerFragment.newInstance(), title = getString(R.string.goal_tracker))
                    selectedTab = SelectedTab.tracker
                }
            }
            R.id.navContacts -> {
                if (selectedTab != SelectedTab.contacts) {
                    toolbar.menu.findItem(R.id.action_help).isVisible = true
                    replaceFragment(ContactsFragment.newInstance(), title = getString(R.string.contacts))
                    selectedTab = SelectedTab.contacts
                }
            }

            R.id.navReachoutLogs -> {
                if (selectedTab != SelectedTab.logs) {
                    toolbar.menu.findItem(R.id.action_help).isVisible = true
                    replaceFragment(ReachoutLogsFragment.newInstance(), title = getString(R.string.reachout_log))
                    selectedTab = SelectedTab.logs
                }
            }

            R.id.navToDos -> {
                if (selectedTab != SelectedTab.todos) {
                    toolbar.menu.findItem(R.id.action_help).isVisible = true
                    replaceFragment(TodosFragment.newInstance(), title = getString(R.string.todos))
                    selectedTab = SelectedTab.todos
                }
            }

            R.id.navMore -> {
                if (selectedTab != SelectedTab.more) {
                    replaceFragment(MoreFragment.newInstance(), title = getString(R.string.more))
                    selectedTab = SelectedTab.more
                    toolbar.menu.findItem(R.id.action_help).isVisible = false
                }
            }
        }
    }

    enum class SelectedTab {
        tracker, contacts, logs, todos, more;

        fun title(): String = when(this) {
            tracker -> HiloApp.instance.getString(R.string.goal_tracker)
            contacts -> HiloApp.instance.getString(R.string.contacts)
            logs -> HiloApp.instance.getString(R.string.reachout_log)
            todos -> HiloApp.instance.getString(R.string.todos)
            more -> HiloApp.instance.getString(R.string.more)
        }

        fun helpText(): String = when(this) {
            tracker -> "The Goal Tracker page is here to keep you focused on the core of your business." +
                    " How do you know if you’re reaching out enough? The Goal Tracker will tell you!\r\n\r\nThe " +
                    "queues for each task will adjust to accommodate the number of weekly reach outs associated " +
                    "with the Goal Plan you've chosen. Fill your New Reach Out and Follow Up queues for the week " +
                    "either by typing contact names in the empty slots or have Hilo do it for you by clicking the " +
                    "“Hilo My Week” button at the top of the page.\r\n\r\nCheck this page regularly! Contacts " +
                    "scheduled for follow up will be added to your follow up queue when the date forfollow up " +
                    "is reached.\r\n\r\nYou can log your reach outs right from this page by clicking the \"Complete\"" +
                    " button next to a contact’s name.\r\n\r\nThe graphs above each queue let you know how close " +
                    "you are to hitting your daily, weekly, monthly, and annual goals. Change the graph views by " +
                    "selecting a different time frame from the drop down menu at the top of the page."
            contacts -> "Welcome to the \"Contact List\" view! Here you’ll see all of your contacts in a list " +
                    "format.\r\n\r\nImport Contacts: By clicking the Import Contacts button in the top-right " +
                    "corner of the page you'll be able to instantly import contacts straight from your phone! " +
                    "To import contacts from Facebook or Excel please log in to the desktop site at www.hiloipa.com." +
                    "\r\n\r\nClick the greentext icon to prepare a text to be sent through the iMessage app in your " +
                    "phone. Texts sent through the app will be recorded inthe Reach Out Log automatically. This is " +
                    "an app only feature and can't be used on the desktop site.\r\n\r\nFilter your list by clicking " +
                    "the \"Filter\" button at the top of the page.\r\n\r\nBulk Features: You can also update multiple " +
                    "contacts by selecting the contacts using the checkboxes to the left, and clicking the “Actions” " +
                    "button towardthe top of the page. From here you can Assign Campaigns, Bulk Update Attributes for " +
                    "a Contact, Add ContactsTo Your Goal Tracker Queues, or Bulk Delete Contacts.\r\n\r\nClick on any " +
                    "name to bring up the contact details page for anyone on your list."
            logs -> "Welcome to the Reach Out Log! On the Reach Out Log page you can see a complete list of all reach" +
                    " outs recorded from anywhere in the system (e.g. the Goal Tracker \"Complete\" menu, Contact Detail" +
                    " page, etc.).\r\n\r\nLognew reach outs by clicking the \"Add Reach Out\" button towards the top-right" +
                    " of the page.\r\n\r\nSearch a contact name in the Search field at the top of the page to see all " +
                    "reach outs logged for that particular contact.\r\n\r\nClicking on a Contact's Name will bring you to " +
                    "their Contact Detail page, and clicking the \"arrow\" icon to the left of a contact's name will allow " +
                    "you to read the reach out details below the contact's name.\r\n\r\nClick the \"pencil\" icon to edit the " +
                    "reach out."
            todos -> "Welcome to the To Do's page! From here you can enter your Goals, the Actions you need to take to " +
                    "achieve those goals, and tasks to support your team which we call Team Needs. You canalso see the " +
                    "calendar events you have scheduled at the bottom of the page.\r\n\r\nTap on any of the sections to" +
                    " the the full list of entries.\r\n\r\nTeam Needs are tasks specifically meant to support your team." +
                    " Some examples of team needs are: scheduling a happy hour for your local team, helping a new " +
                    "consultant plan their business launch party, posting in your group Facebook page, etc."
            more -> ""
        }
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
                val deviceContact = DeviceContact(phone)
                // try to get contact image
                var photo: Uri? = null
                val photos = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                        contactId.toLong())
                photo = Uri.withAppendedPath(photos, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                deviceContact.photoPath = photo
                // set other contact data
                deviceContact.email = email
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        supportFragmentManager.fragments.forEach { it.onRequestPermissionsResult(requestCode, permissions, grantResults) }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
    }
}
