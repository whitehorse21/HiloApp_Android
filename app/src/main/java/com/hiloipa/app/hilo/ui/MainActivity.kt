package com.hiloipa.app.hilo.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.RadioGroup
import com.hiloipa.app.hilo.ui.auth.AuthActivity
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.contacts.ContactsFragment
import com.hiloipa.app.hilo.ui.more.MoreFragment
import com.hiloipa.app.hilo.ui.reachout.ReachoutLogsFragment
import com.hiloipa.app.hilo.ui.todos.TodosFragment
import com.hiloipa.app.hilo.ui.tracker.GoalTrackerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    val goalTrackerFragment = GoalTrackerFragment.newInstance()
    val contactsFragment = ContactsFragment.newInstance()
    val reachoutLogsFragment = ReachoutLogsFragment.newInstance()
    val todosFragment = TodosFragment.newInstance()
    val moreFragment = MoreFragment.newInstance()
    var selectedTab: SelectedTab = SelectedTab.tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnCheckedChangeListener(this)
//        startActivity(Intent(this, AuthActivity::class.java))

        replaceFragment(goalTrackerFragment, title = getString(R.string.goal_tracker))
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
        when(checkedId) {
            R.id.navGoalTracker -> {
                if (selectedTab != SelectedTab.tracker) {
                    replaceFragment(goalTrackerFragment, title = getString(R.string.goal_tracker))
                    selectedTab = SelectedTab.tracker
                }
            }
            R.id.navContacts -> {
                if (selectedTab != SelectedTab.contacts) {
                    replaceFragment(contactsFragment, title = getString(R.string.contacts))
                    selectedTab = SelectedTab.contacts
                }
            }

            R.id.navReachoutLogs -> {
                if (selectedTab != SelectedTab.logs) {
                    replaceFragment(reachoutLogsFragment, title = getString(R.string.reachout_log))
                    selectedTab = SelectedTab.logs
                }
            }

            R.id.navToDos -> {
                if (selectedTab != SelectedTab.todos) {
                    replaceFragment(todosFragment, title = getString(R.string.todos))
                    selectedTab = SelectedTab.todos
                }
            }

            R.id.navMore -> {
                if (selectedTab != SelectedTab.more) {
                    replaceFragment(moreFragment, title = getString(R.string.more))
                    selectedTab = SelectedTab.more
                }
            }
        }
    }

    enum class SelectedTab {
        tracker, contacts, logs, todos, more
    }
}
