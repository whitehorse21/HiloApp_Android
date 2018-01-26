package com.hiloipa.app.hilo.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.hiloipa.app.hilo.ui.auth.AuthActivity
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.tracker.GoalTrackerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val goalTrackerFragment = GoalTrackerFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
