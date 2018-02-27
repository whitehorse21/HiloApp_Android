package com.hiloipa.app.hilo.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.CustomField

/**
 * Created by eduardalbu on 23.02.2018.
 */
class CustomFieldView: LinearLayout {

    private lateinit var field: RalewayEditText
    private lateinit var deleteBtn: ImageButton
    private lateinit var fieldTitle: RalewayTextView
    lateinit var customField: CustomField

    constructor(context: Context, customField: CustomField) : super(context) {
        initialize(context)
        this.customField = customField
        this.text(customField.fieldValue)
        this.hint(customField.fieldName)
        this.deleteBtn.tag = customField
        this.tag = customField
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context)
    }

    fun initialize(context: Context) {
        inflate(context, R.layout.layout_custom_field, this)
        field = this.findViewById(R.id.customFieldInput)
        deleteBtn = this.findViewById(R.id.deleteBtn)
        fieldTitle = this.findViewById(R.id.fieldTItle)
    }

    fun text(text: String) {
        this.field.setText(text.replace("_", " "))
    }

    fun text(): String = this.field.text.toString()

    fun hint(hint: String) {
        this.field.setHint(hint)
        this.fieldTitle.text = hint
    }

    fun hint(): String = this.field.hint.toString()

    fun deleteClickListener(clickListener: OnClickListener) {
        deleteBtn.setOnClickListener(clickListener)
    }
}