package com.hilocrm.app.hilo.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import com.hilocrm.app.hilo.R
import kotlin.properties.Delegates


/**
 * Created by eduardalbu on 05.02.2018.
 */
class ColorButton: View {

    var color: Int by Delegates.observable(resources.getColor(R.color.colorWhite)) { property, oldValue, newValue ->
        init(context = context)
    }

    var isChecked: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        init(context = context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        getAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        getAttributes(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
        getAttributes(context, attrs)
    }

    private fun init(context: Context) {
        val bgRes = if (isChecked) R.drawable.circle_dark_yellow else R.drawable.rectangle_fill_orange
        setBackgroundResource(bgRes)
        val drawable = background as GradientDrawable
        drawable.setColor(color)
    }

    fun getAttributes(contex: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = contex.obtainStyledAttributes(attrs, R.styleable.ColorButton)
        this.isChecked = typedArray.getBoolean(R.styleable.ColorButton_checked, false)
        this.color = typedArray.getColor(R.styleable.ColorButton_color, resources.getColor(R.color.colorWhite))
        typedArray.recycle()
    }
}