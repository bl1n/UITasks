package team.lf.uitasks.bubbles

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import team.lf.uitasks.ChooseFragment
import team.lf.uitasks.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SurfaceViewBubblesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): SurfaceViewBubblesFragment =
            SurfaceViewBubblesFragment()

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

    override fun onDetach() {
        super.onDetach()
        gameView.detach()
    }


    inner class GameView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : SurfaceView(context, attrs, defStyleAttr), Runnable {

        private lateinit var timer: CountDownTimer
        private var isRunning: Boolean = false
        private var gameThread: Thread? = null

        private var bubbleList: List<Bubble> = emptyList()
        private var paint = Paint()
        var counter = 0

        init {
            paint.apply {
                color = Color.DKGRAY
                isAntiAlias = true
                isDither = true
                style = Paint.Style.FILL
            }

            startNewGame()
        }

        private fun startNewGame() {
            val numberOfBubbles = Random.nextInt(1, 8)
            counter = numberOfBubbles * 2
            isRunning = true
            bubbleList = getListOfBubbles(numberOfBubbles)
            resume()

            timer = object : CountDownTimer((numberOfBubbles * 2 * 1000 - 1000).toLong(), 950) {
                override fun onFinish() {
                    startDialog("Время вышло!")
                }

                override fun onTick(millisUntilFinished: Long) {
                    counter--
                    showMessage(counter.toString())
                }
            }
            timer.start()
        }

        override fun run() {
            var canvas: Canvas
            while (isRunning) {
                if (holder.surface.isValid) {
                    canvas = holder.lockCanvas()
                    canvas.drawColor(Color.BLUE)
                    bubbleList.forEach {
                        canvas.drawCircle(it.x, it.y, it.radius, paint)
                        if (it.pointerId == null) {
                            it.checkEdges()
                            it.addDeltas()
                        }
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }


        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    checkTouchEvent(event.x, event.y, event.actionIndex)
                    checkWin()
                }
                MotionEvent.ACTION_POINTER_DOWN -> {

                    val x = event.getX(event.actionIndex)
                    val y = event.getY(event.actionIndex)
                    checkTouchEvent(x, y, event.getPointerId(event.actionIndex))
                    checkWin()
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    bubbleList.forEach {
                        if (it.pointerId == event.getPointerId(event.actionIndex)) {
                            it.onResumeMotion()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    resumeMotion()
                }
            }
            return true
        }

        private fun resumeMotion() {
            bubbleList.forEach {
                it.onResumeMotion()
            }
        }

        private fun showMessage(string: String) {
            Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show()
        }

        private fun checkTouchEvent(eventX: Float, eventY: Float, actionId: Int) {
            bubbleList.forEach {
                val distanceToX = eventX - it.x
                val distanceToY = eventY - it.y
                val isInside =
                    distanceToX * distanceToX + distanceToY * distanceToY <= BUBBLE_RADIUS * BUBBLE_RADIUS
                if (isInside) {
                    it.onPauseMotion(actionId)
                    return
                }
            }
        }

        private fun checkWin() {
            bubbleList.forEach {
                if (it.pointerId == null) return
            }
            startDialog("Победа!")
        }

        private fun startDialog(string: String) {
            timer.cancel()
            pause()
            val builderDialog = AlertDialog.Builder(requireActivity())
            builderDialog.setTitle(string)
            builderDialog.setPositiveButton("Повторим!") { dialog, _ ->
                dialog.cancel()
                startNewGame()
            }
            builderDialog.setNegativeButton("Больше не хочу!") { dialog, _ ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        ChooseFragment.newInstance()
                    )
                    .commit()
                dialog.cancel()
            }
                .setCancelable(false)
            builderDialog.create().show()
        }

        private fun getListOfBubbles(count: Int): List<Bubble> {
            val list = mutableListOf<Bubble>()
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val rootWidth = displayMetrics.widthPixels
            val rootHeight = displayMetrics.heightPixels
            val leftEdge = 0f + BUBBLE_RADIUS
            val topEdge = 0f + BUBBLE_RADIUS
            val rightEdge = rootWidth - BUBBLE_RADIUS
            val bottomNavBarHeight =
                resources.getDimension(
                    resources.getIdentifier(
                        "navigation_bar_height",
                        "dimen",
                        "android"
                    )
                )
            val bottomEdge =
                rootHeight - BUBBLE_RADIUS - bottomNavBarHeight

            for (i in 0 until count) {
                val x = Random.nextInt(leftEdge.toInt(), rightEdge.toInt()).toFloat()
                val y = Random.nextInt(topEdge.toInt(), bottomEdge.toInt()).toFloat()
                val c = Random.nextInt(12, 20)
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
                        BUBBLE_RADIUS
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
//            resumeMotion()
            isRunning = true
            gameThread = Thread(this)
            gameThread?.start()
        }

        fun detach() {
            timer.cancel()
        }

    }
}

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

private fun Int.toRad() = this * PI / 180
