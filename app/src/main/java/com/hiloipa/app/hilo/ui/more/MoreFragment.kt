package com.hiloipa.app.hilo.ui.more


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.more.email.EmailTemplatesActivity
import com.hiloipa.app.hilo.ui.more.notes.NotepadActivity
import com.hiloipa.app.hilo.ui.more.products.ProductsActivity
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
            val templatesIntent = Intent(activity, NotepadActivity::class.java)
            activity.startActivity(templatesIntent)
        }

        feedbackBtn.setOnClickListener {
            val feedbackIntent = Intent(activity, FeedbackActivity::class.java)
            activity.startActivity(feedbackIntent)
        }

        productsBtn.setOnClickListener {
            val feedbackIntent = Intent(activity, ProductsActivity::class.java)
            activity.startActivity(feedbackIntent)
        }
    }
}
