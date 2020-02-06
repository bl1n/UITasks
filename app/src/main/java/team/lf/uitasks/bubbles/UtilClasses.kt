package team.lf.uitasks.bubbles

import android.graphics.Color
import android.graphics.Paint
import kotlin.math.PI

class Bubble(
    private var leftEdge: Float,
    private var topEdge: Float,
    private var rightEdge: Float,
    private var bottomEdge: Float,
    var x: Float,
    var y: Float,
    private var deltaX: Float,
    private var deltaY: Float,
    var radius: Float
) {
    var pointerId: Int? = null

    fun addDeltas() {
        x += deltaX
        y += deltaY
    }

    fun checkEdges() {
        if (x <= leftEdge || x >= rightEdge) {
            deltaX = -deltaX
        }
        if (y <= topEdge || y >= bottomEdge) {
            deltaY = -deltaY
        }
    }

    fun onPauseMotion(index: Int) {
        pointerId = index
    }

    fun onResumeMotion() {
        pointerId = null
    }

}

class Counter(countLimit: Int) {
    val paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        isAntiAlias = true
        textSize = 150f
    }
    var count = countLimit

    fun nextSecond() {
        count--
    }
}

