package com.hiloipa.app.hilo.ui.more.notes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.NoteColor
import com.hiloipa.app.hilo.ui.widget.ColorButton
import kotlinx.android.synthetic.main.activity_create_note.*

class CreateNoteActivity : AppCompatActivity(), View.OnClickListener {

    var colorBtns: MutableList<ColorButton> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        toolbar.setNavigationOnClickListener { finish() }

        addTagBtn.setOnClickListener {
            TagsFragment.newInstance().show(supportFragmentManager, "TagsFragment")
        }

        pickColorBtn.setOnClickListener {
            colorsLayout.visibility = if (colorsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        colorBtns = mutableListOf(whiteBtn, redBtn, roseBtn, purpleBtn, darkBlueBtn,
                blueBtn, skyBlueBtn, oliveBtn, greenBtn, chartreuseBtn, yellowBtn,
                darkYellowBtn, brownBtn, grayBtn)
        colorBtns.forEach { it.setOnClickListener(this) }

        cancelBtn.setOnClickListener { finish() }
    }

    override fun onClick(v: View) {
        colorBtns.forEach { it.isChecked = it.id == v.id }
        val color = NoteColor.fromString(colorBtns.first { it.isChecked }.tag as String)
        noteCard.setCardBackgroundColor(resources.getColor(color.colorRes()))
        noteTitleField.setHintTextColor(resources.getColor(color.textColor()))
        noteTitleField.setTextColor(resources.getColor(color.textColor()))
        noteContentField.setHintTextColor(resources.getColor(color.textColor()))
        noteContentField.setTextColor(resources.getColor(color.textColor()))
        addTagBtn.setImageResource(color.tagIcon())
        pickColorBtn.setImageResource(color.colorIcon())
    }
}
