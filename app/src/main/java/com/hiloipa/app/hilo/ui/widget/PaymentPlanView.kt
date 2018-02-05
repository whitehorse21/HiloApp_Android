package com.hiloipa.app.hilo.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.hiloipa.app.hilo.R
import kotlin.properties.Delegates

/**
 * Created by eduardalbu on 05.02.2018.
 */
class PaymentPlanView: LinearLayout {

    lateinit var titleLabel: RalewayTextView
    lateinit var priceLabel: RalewayTextView

    var isChecked: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        if (newValue) {
            this.setBackgroundResource(R.drawable.plan_bg_card_selected)
            invalidate()
            titleLabel.setBackgroundResource(R.drawable.plan_bg_header_selected)
            priceLabel.setBackgroundResource(R.drawable.plan_bg_price_selected)
        } else {
            this.setBackgroundResource(R.drawable.plan_bg_card)
            invalidate()
            titleLabel.setBackgroundResource(R.drawable.plan_bg_header)
            priceLabel.setBackgroundResource(R.drawable.plan_bg_price)
        }
    }

    var title: String by Delegates.observable("") { property, oldValue, newValue ->
        this.titleLabel.text = newValue
    }

    var price: String by Delegates.observable("$0/month") { property, oldValue, newValue ->
        priceLabel.text = newValue
    }

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
        getAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
        getAttributes(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context)
        getAttributes(context, attrs)
    }

    private fun initialize(context: Context) {
        inflate(context, R.layout.layout_payment_plan, this)
        this.titleLabel = this.findViewById(R.id.titleLabel)
        this.priceLabel = this.findViewById(R.id.priceLabel)
    }

    fun getAttributes(contex: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = contex.obtainStyledAttributes(attrs, R.styleable.PaymentPlanView)
        this.isChecked = typedArray.getBoolean(R.styleable.PaymentPlanView_selected, false)
        this.title = typedArray.getString(R.styleable.PaymentPlanView_planName)
        this.price = typedArray.getString(R.styleable.PaymentPlanView_planPrice)
        typedArray.recycle()
    }
}