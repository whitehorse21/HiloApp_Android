package com.hilocrm.app.hilo.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import com.hilocrm.app.hilo.R

/**
 * Created by eduardalbu on 31.01.2018.
 */
class EditCustomField: LinearLayout, View.OnClickListener {

    lateinit var field: EditText
    lateinit var deleteBtn: ImageButton
    var delegate: FieldListener? = null

    companion object {
        fun createInstance(hint: String = "", tag: Int, context: Context): EditCustomField {
            val field = EditCustomField(context)
            field.tag = tag
            field.field.hint = hint
            return field
        }
    }

    constructor(context: Context) : super(context) {
        this.initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initialize(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.initialize(context)
    }

    private fun initialize(context: Context) {
        inflate(context, R.layout.layout_custom_field, this)
        this.field = findViewById(R.id.customFieldInput)
        this.deleteBtn = findViewById(R.id.deleteBtn)
        deleteBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        this.delegate?.onDeleteClicked(this.tag as Int, fieldText = field.text.toString())
    }

    fun text(): String = field.text.toString()

    interface FieldListener {
        fun onDeleteClicked(tag: Int, fieldText: String)
    }
}