package com.hiloipa.app.hilo.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.hiloipa.app.hilo.R
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 26.01.2018.
 */
class RalewayTabLayout: TabLayout {

    var fontType: FontType by Delegates.observable(FontType.regular) { property, oldValue, newValue ->
        if (this == null) return@observable
        val viewGroup = this as ViewGroup
        for (index in 0..childCount) {
            val tab = viewGroup.getChildAt(index) as ViewGroup
            for (childIndex in 0..tab.childCount) {
                val view = tab.getChildAt(childIndex)
                if (view is TextView) {
                    view.setTypeface(Typeface.createFromAsset(context.assets, "Font/${newValue.fontName()}"))
                }
            }
        }
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttributes(context, attrs)
    }

    fun getAttributes(contex: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = contex.obtainStyledAttributes(attrs, R.styleable.RalewayTabLayout)
        val intValue = typedArray.getInt(R.styleable.RalewayTabLayout_tabFont, 0)
        this.fontType = FontType.fromInt(intValue = intValue)
        typedArray.recycle()
    }
}