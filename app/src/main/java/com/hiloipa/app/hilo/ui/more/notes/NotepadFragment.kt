package com.hiloipa.app.hilo.ui.more.notes


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.UserNotesAdapter
import kotlinx.android.synthetic.main.fragment_notepad.*


/**
 * A simple [Fragment] subclass.
 */
class NotepadFragment : Fragment(), UserNotesAdapter.UserNoteDelegate {

    lateinit var adapter: UserNotesAdapter

    companion object {
        fun newInstance(): NotepadFragment {
            val args = Bundle()
            val fragment = NotepadFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_notepad, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserNotesAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        addNoteBtn.setOnClickListener {
            val intent = Intent(activity, CreateNoteActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onEditNoteClicked() {
        val intent = Intent(activity, CreateNoteActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onDeleteNoteClicked() {

    }
}
