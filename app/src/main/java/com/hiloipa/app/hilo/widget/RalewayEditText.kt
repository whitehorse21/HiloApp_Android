package com.hiloipa.app.hilo.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import com.hiloipa.app.hilo.R
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 25.01.2018.
 */
class RalewayEditText: EditText {
    var fontType: FontType by Delegates.observable(FontType.regular) { property, oldValue, newValue ->
        typeface = Typeface.createFromAsset(context.assets, "Font/${newValue.fontName()}")
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttributes(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        getAttributes(context, attrs)
    }

    fun getAttributes(contex: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = contex.obtainStyledAttributes(attrs, R.styleable.RalewayEditText)
        val intValue = typedArray.getInt(R.styleable.RalewayEditText_fieldFont, 0)
        this.fontType = FontType.fromInt(intValue = intValue)
        typedArray.recycle()
    }
}