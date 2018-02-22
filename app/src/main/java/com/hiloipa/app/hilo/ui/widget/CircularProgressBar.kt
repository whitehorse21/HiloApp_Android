package com.hiloipa.app.hilo.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.hiloipa.app.hilo.utils.HiloApp

/**
 * Simple single android view component that can be used to showing a round progress bar.
 * It can be customized with size, stroke size, colors and text etc.
 * Progress change will be animated.
 * Created by Kristoffer, http://kmdev.se
 */
class CircularProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    private val mStartAngle = -90f      // Always start from top (default is: "3 o'clock on a watch.")
    private var mSweepAngle = 0f              // How long to sweep from mStartAngle
    private val mMaxSweepAngle = 360f         // Max degrees to sweep = full circle
    private var mStrokeWidth = 20              // Width of outline
    private val mAnimationDuration = 400       // Animation duration for progress change
    private val mMaxProgress = 100             // Max progress to use
    private var mDrawText = true           // Set to true if progress text should be drawn
    private var mRoundedCorners = true     // Set to true if rounded corners should be applied to outline ends
    private var mProgressColor = Color.BLACK   // Outline color
    private var mTextColor = Color.BLACK       // Progress text color

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)                 // Allocate paint outside onDraw to avoid unnecessary object creation

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initMeasurments()
        drawOutlineArc(canvas)

        if (mDrawText) {
            drawText(canvas)
        }
    }

    private fun initMeasurments() {
        mViewWidth = width
        mViewHeight = height
    }

    private fun drawOutlineArc(canvas: Canvas) {

        val diameter = Math.min(mViewWidth, mViewHeight)
        val pad = (mStrokeWidth / 2.0).toFloat()
        val outerOval = RectF(pad, pad, diameter - pad, diameter - pad)

        mPaint.color = mProgressColor
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = if (mRoundedCorners) Paint.Cap.ROUND else Paint.Cap.BUTT
        mPaint.style = Paint.Style.STROKE
        canvas.drawArc(outerOval, mStartAngle, mSweepAngle, false, mPaint)
    }

    private fun drawText(canvas: Canvas) {
        mPaint.textSize = Math.min(mViewWidth, mViewHeight) / 5f
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.typeface = Typeface.createFromAsset(context.assets, "Font/${FontType.bold.fontName()}")
        mPaint.strokeWidth = 0f
        mPaint.color = mTextColor

        // Center text
        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (mPaint.descent() + mPaint.ascent()) / 2).toInt()

        canvas.drawText(calcProgressFromSweepAngle(mSweepAngle).toString() + "%", xPos.toFloat(), yPos.toFloat(), mPaint)
    }

    private fun calcSweepAngleFromProgress(progress: Int): Float {
        return mMaxSweepAngle / mMaxProgress * progress
    }

    private fun calcProgressFromSweepAngle(sweepAngle: Float): Int {
        return (sweepAngle * mMaxProgress / mMaxSweepAngle).toInt()
    }

    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    fun setProgress(progress: Int) {
        val animator = ValueAnimator.ofFloat(mSweepAngle, calcSweepAngleFromProgress(progress))
        animator.interpolator = DecelerateInterpolator()
        animator.duration = mAnimationDuration.toLong()
        animator.addUpdateListener { valueAnimator ->
            mSweepAngle = valueAnimator.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    fun setProgressColor(color: Int) {
        mProgressColor = color
        invalidate()
    }

    fun setProgressWidth(width: Int) {
        mStrokeWidth = width
        invalidate()
    }

    fun setTextColor(color: Int) {
        mTextColor = color
        invalidate()
    }

    fun showProgressText(show: Boolean) {
        mDrawText = show
        invalidate()
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    fun useRoundedCorners(roundedCorners: Boolean) {
        mRoundedCorners = roundedCorners
        invalidate()
    }
}