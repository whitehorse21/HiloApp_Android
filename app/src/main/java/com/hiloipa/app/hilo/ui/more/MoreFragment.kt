package com.hiloipa.app.hilo.ui.more


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.auth.AuthActivity
import com.hiloipa.app.hilo.ui.more.account.AccountActivity
import com.hiloipa.app.hilo.ui.more.email.EmailTemplatesActivity
import com.hiloipa.app.hilo.ui.more.notes.NotepadActivity
import com.hiloipa.app.hilo.ui.more.products.ProductsActivity
import com.hiloipa.app.hilo.ui.more.scripts.ScriptsActivity
import kotlinx.android.synthetic.main.fragment_more.*


/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : Fragment() {

    companion object {
        fun newInstance(): MoreFragment {
            val args = Bundle()
            val fragment = MoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_more, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        templatesBtn.setOnClickListener {
            val templatesIntent = Intent(activity, EmailTemplatesActivity::class.java)
            activity.startActivity(templatesIntent)
        }

        notepadBtn.setOnClickListener {
            val notepadIntent = Intent(activity, NotepadActivity::class.java)
            activity.startActivity(notepadIntent)
        }

        feedbackBtn.setOnClickListener {
            val feedbackIntent = Intent(activity, FeedbackActivity::class.java)
            activity.startActivity(feedbackIntent)
        }

        productsBtn.setOnClickListener {
            val productsIntent = Intent(activity, ProductsActivity::class.java)
            activity.startActivity(productsIntent)
        }

        logoutBtn.setOnClickListener {
            val logoutIntent = Intent(activity, AuthActivity::class.java)
            activity.startActivity(logoutIntent)
            activity.finish()
        }

        accountBtn.setOnClickListener {
            val accountIntent = Intent(activity, AccountActivity::class.java)
            activity.startActivity(accountIntent)
        }

        scriptsBtn.setOnClickListener {
            val scriptsIntent = Intent(activity, ScriptsActivity::class.java)
            activity.startActivity(scriptsIntent)
        }
    }
}
