package com.hilocrm.app.hilo.ui.more.notes


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hilocrm.app.hilo.R
import com.hilocrm.app.hilo.adapter.UserNotesAdapter
import com.hilocrm.app.hilo.models.requests.DeleteNoteRequest
import com.hilocrm.app.hilo.models.requests.StandardRequest
import com.hilocrm.app.hilo.models.responses.HiloResponse
import com.hilocrm.app.hilo.models.responses.Note
import com.hilocrm.app.hilo.models.responses.NoteTag
import com.hilocrm.app.hilo.models.responses.NotepadNotes
import com.hilocrm.app.hilo.ui.base.BaseFragment
import com.hilocrm.app.hilo.utils.HiloApp
import com.hilocrm.app.hilo.utils.isSuccess
import com.hilocrm.app.hilo.utils.showExplanation
import com.hilocrm.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notepad.*


/**
 * A simple [Fragment] subclass.
 */
class NotepadFragment : BaseFragment(), UserNotesAdapter.UserNoteDelegate {

    lateinit var adapter: UserNotesAdapter

    companion object {
        fun newInstance(): NotepadFragment {
            val args = Bundle()
            val fragment = NotepadFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_notepad, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserNotesAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        addNoteBtn.setOnClickListener {
            val intent = Intent(activity, CreateNoteActivity::class.java)
            activity!!.startActivityForResult(intent, 1908)
        }

        getNotes()
    }

    override fun onEditNoteClicked(note: Note, position: Int) {
        val intent = Intent(activity, CreateNoteActivity::class.java)
        intent.putExtra(CreateNoteActivity.noteKey, note)
        activity!!.startActivityForResult(intent, 1908)
    }

    override fun onDeleteNoteClicked(note: Note, position: Int) {
        val alert = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete_note))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity!!.showLoading()
                    val request = DeleteNoteRequest()
                    request.noteId = "${note.id}"
                    HiloApp.api().deleteNote(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    adapter.data.remove(note)
                                    adapter.notifyItemRemoved(position)
                                } else activity!!.showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity!!.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        alert.show()
    }

    override fun onDeleteTagClicked(note: Note, notePosition: Int, tag: NoteTag, position: Int) {
        val alert = AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete_tag))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity!!.showLoading()
                    val request = DeleteNoteRequest()
                    request.noteId = "${note.id}"
                    request.tagId = tag.name
                    HiloApp.api().deleteTagFromNote(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    getNotes()
                                } else activity!!.showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity!!.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        alert.show()
    }

    private fun getNotes() {
        val loading = activity!!.showLoading()
        HiloApp.api().getNotepadNotes(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<NotepadNotes> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            adapter.refreshData(data = data.notes)
                    } else activity!!.showExplanation(message = response.message)
                }, { error ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                1908 -> getNotes()
            }
        }
    }
}
