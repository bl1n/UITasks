package team.lf.uitasks.bubbles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val BACKGROUND_COLOR = Color.WHITE
private const val BUBBLE_COLOR = Color.GRAY
private const val INNER_BUBBLE_COLOR = Color.GRAY

class BubbleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val innerBubblePaint = Paint().apply {
        color = INNER_BUBBLE_COLOR
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }

    private val initBubblePaint = Paint().apply {
        color = BUBBLE_COLOR
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(BACKGROUND_COLOR)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas.drawCircle((width/2).toFloat(), (width/2).toFloat(), (width/2).toFloat(), initBubblePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchDown()
            MotionEvent.ACTION_UP -> touchUp()
        }
       return true
    }

    private fun touchDown() {
        extraCanvas.drawColor(BACKGROUND_COLOR)
        Log.d("TAG", "down ${(width/2).toFloat()}")
        extraCanvas.drawCircle((width/2).toFloat(), (width/2).toFloat(), (width/2).toFloat(), innerBubblePaint)
        invalidate()
    }

    private fun touchUp() {
        extraCanvas.drawColor(BACKGROUND_COLOR)
        Log.d("TAG", "up")
        extraCanvas.drawCircle((width/2).toFloat(), (width/2).toFloat(), (width/2).toFloat(), initBubblePaint)
        invalidate()
    }
}