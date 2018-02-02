package com.hiloipa.app.hilo.ui.contacts


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.ContactsAdapter
import com.hiloipa.app.hilo.utils.ContactsDelegate
import kotlinx.android.synthetic.main.fragment_contacts.*


/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment(), ContactsDelegate {

    lateinit var adapter: ContactsAdapter

    companion object {
        fun newInstance(): ContactsFragment {
            val args = Bundle()
            val fragment = ContactsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_contacts, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(activity)

        addContactBtn.setOnClickListener {
            val intent = Intent(activity, EditContactActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onContactClicked() {
        val intent = Intent(activity, ContactDetailsActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onEditContactClicked() {
        val intent = Intent(activity, EditContactActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onDeleteContactClicked() {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                })
                .setNegativeButton(getString(R.string.no), { dialog, which ->
                    dialog.dismiss()
                })
                .create()

        dialog.show()
    }

    override fun onSendSmsClicked() {

    }
}
