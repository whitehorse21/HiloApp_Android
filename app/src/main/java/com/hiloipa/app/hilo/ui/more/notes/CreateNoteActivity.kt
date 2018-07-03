package com.hiloipa.app.hilo.ui.more.notes

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.NoteColor
import com.hiloipa.app.hilo.models.requests.SaveNoteRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.Note
import com.hiloipa.app.hilo.models.responses.NoteTag
import com.hiloipa.app.hilo.ui.base.BaseActivity
import com.hiloipa.app.hilo.ui.widget.ColorButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_note.*

class CreateNoteActivity : BaseActivity(), View.OnClickListener {

    var colorBtns: MutableList<ColorButton> = mutableListOf()
    var selectedTags: ArrayList<NoteTag> = arrayListOf()
    var note: Note? = null
    var noteColor = NoteColor.white

    companion object {
        const val noteKey = "com.hiloipa.app.hilo.ui.more.notes.NOTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        val filter = IntentFilter(TagsFragment.actionTagsSelected)
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, filter)

        colorBtns = mutableListOf(whiteBtn, redBtn, roseBtn, purpleBtn, darkBlueBtn,
                blueBtn, skyBlueBtn, oliveBtn, greenBtn, chartreuseBtn, yellowBtn,
                darkYellowBtn, brownBtn, grayBtn)
        colorBtns.forEach { it.setOnClickListener(this) }

        if (intent.extras != null && intent.extras.containsKey(noteKey)) {
            note = intent.extras.getParcelable(noteKey)
            selectedTags = note!!.tags()
            noteTitleField.setText(note!!.title)
            noteContentField.setText(note!!.content)
            saveBtn.text = getString(R.string.save)
            val colorBtn = colorBtns.firstOrNull { it.tag == note!!.notColor }
            if (colorBtn != null)
                onClick(colorBtn)
        }

        addTagBtn.setOnClickListener {
            TagsFragment.newInstance(selectedTags).show(supportFragmentManager, "TagsFragment")
        }

        pickColorBtn.setOnClickListener {
            colorsLayout.visibility = if (colorsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        cancelBtn.setOnClickListener { finish() }

        saveBtn.setOnClickListener { saveNote() }
    }

    override fun onClick(v: View) {
        colorBtns.forEach { it.isChecked = it.id == v.id }
        noteColor = NoteColor.fromString(colorBtns.first { it.isChecked }.tag as String)
        noteCard.setCardBackgroundColor(resources.getColor(noteColor.colorRes()))
        noteTitleField.setHintTextColor(resources.getColor(noteColor.textColor()))
        noteTitleField.setTextColor(resources.getColor(noteColor.textColor()))
        noteContentField.setHintTextColor(resources.getColor(noteColor.textColor()))
        noteContentField.setTextColor(resources.getColor(noteColor.textColor()))
        addTagBtn.setImageResource(noteColor.tagIcon())
        pickColorBtn.setImageResource(noteColor.colorIcon())

    }

    private fun saveNote() {
        val noteTitle = noteTitleField.text.toString()
        val noteContent = noteContentField.text.toString()
        if (noteTitle.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_note_title),
                    Toast.LENGTH_SHORT).show()
            return
        }

        if (noteContent.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_note_content),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val request = SaveNoteRequest()
        request.noteColor = noteColor.stringColor()
        request.noteText = noteContent
        request.noteTitle = noteTitle
        request.noteId = "${note?.id ?: "0"}"

        var tags = ""
        selectedTags.forEach { tags = "$tags,${it.name}" }
        tags = "(,$)*".toRegex().replace(tags, "")
        request.tags = tags

        val loading = showLoading()
        HiloApp.api().saveUserNote(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when (intent.action) {
                TagsFragment.actionTagsSelected -> {
                    selectedTags = intent.extras.getParcelableArrayList("tags")
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(broadcastReceiver)
    }
}
