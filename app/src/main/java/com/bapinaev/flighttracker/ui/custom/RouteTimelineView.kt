package com.bapinaev.flighttracker.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.bapinaev.flighttracker.R

class RouteTimelineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp(3f)
        color = ContextCompat.getColor(context, R.color.stroke_soft)
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.brand_blue)
    }

    private var transfers: Int = 0

    fun setTransfers(count: Int) {
        transfers = count.coerceAtLeast(0).coerceAtMost(3)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = dp(34f).toInt()
        val h = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2f
        val left = paddingLeft + dp(8f)
        val right = width - paddingRight - dp(8f)

        canvas.drawLine(left, centerY, right, centerY, linePaint)

        val dots = transfers + 2
        val step = if (dots > 1) (right - left) / (dots - 1) else 0f

        repeat(dots) { index ->
            val x = left + step * index
            val radius = if (index == 0 || index == dots - 1) dp(6f) else dp(4f)
            canvas.drawCircle(x, centerY, radius, pointPaint)
        }
    }

    private fun dp(value: Float): Float = value * resources.displayMetrics.density
}
