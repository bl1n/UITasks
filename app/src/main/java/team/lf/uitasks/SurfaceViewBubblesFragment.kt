package team.lf.uitasks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SurfaceViewBubblesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): SurfaceViewBubblesFragment = SurfaceViewBubblesFragment()

        const val BUBBLE_RADIUS = 130f
    }

    private lateinit var gameView: GameView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameView = GameView(requireContext())
        return gameView
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    inner class GameView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : SurfaceView(context, attrs, defStyleAttr), Runnable {


        private var isRunning: Boolean = false
        private var gameThread: Thread? = null

        private var bubbleList: List<Bubble>
        private var paint = Paint()

        init {
            paint.apply {
                color = Color.DKGRAY
                isAntiAlias = true
                isDither = true
                style = Paint.Style.FILL
            }
            bubbleList = getListOfBubbles(Random.nextInt(1, 7))
        }

        override fun run() {
            var canvas: Canvas
            while (isRunning) {
                if (holder.surface.isValid) {
                    canvas = holder.lockCanvas()
                    canvas.save()
                    canvas.drawColor(Color.BLUE)
                    bubbleList.forEach {
                        canvas.drawCircle(it.x, it.y, it.radius, paint)
                        it.checkEdges()
                        it.addDeltas()
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun getListOfBubbles(count: Int): List<Bubble> {
            val list = mutableListOf<Bubble>()
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val bubbleRadius = BUBBLE_RADIUS
            val rootWidth = displayMetrics.widthPixels
            val rootHeight = displayMetrics.heightPixels
            val leftEdge = 0f + bubbleRadius
            val topEdge = 0f + bubbleRadius
            val rightEdge = rootWidth - bubbleRadius
            val bottomEdge = rootHeight - bubbleRadius

            for (i in 0 until count) {
                val x = Random.nextInt(leftEdge.toInt(), rightEdge.toInt()).toFloat()
                val y = Random.nextInt(topEdge.toInt(), bottomEdge.toInt()).toFloat()
                val c = Random.nextInt(15, 25)
                val angle = Random.nextInt(0, 360).toRad().toFloat()
                val deltaX = c * cos(angle)
                val deltaY = c * sin(angle)

                list.add(
                    Bubble(
                        leftEdge,
                        topEdge,
                        rightEdge,
                        bottomEdge,
                        x,
                        y,
                        deltaX,
                        deltaY,
                        bubbleRadius
                    )
                )
            }
            return list
        }

        fun pause() {
            isRunning = false
            try {
                gameThread?.join()
            } catch (e: InterruptedException) {
            }

        }

        fun resume() {
            isRunning = true
            gameThread = Thread(this)
            gameThread?.start()
        }
    }
}

class Bubble(
    var leftEdge: Float,
    var topEdge: Float,
    var rightEdge: Float,
    var bottomEdge: Float,
    var x: Float,
    var y: Float,
    var deltaX: Float,
    var deltaY: Float,
    var radius:Float
) {
    private var deltaXCache = 0f

    private var deltaYCache = 0f

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

    fun onPauseMotion(){
        saveAndNullDeltas()
    }

    fun onResumeMotion(){
        restoreDeltas()
    }

    private fun saveAndNullDeltas(){
        deltaXCache = deltaX
        deltaX = 0f
        deltaYCache = deltaY
        deltaY = 0f
    }

    private fun restoreDeltas(){
        deltaX = deltaXCache
        deltaY = deltaYCache
    }
}

private fun Int.toRad() = this * PI / 180
