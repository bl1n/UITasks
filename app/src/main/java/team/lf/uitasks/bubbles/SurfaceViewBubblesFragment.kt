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

        private var rootHeight: Int
        private var rootWidth: Int
        private var bottomEdge: Float
        private var rightEdge: Float
        private var topEdge: Float
        private var leftEdge: Float
        private lateinit var timer: CountDownTimer
        private var isRunning: Boolean = false
        private var gameThread: Thread? = null

        private var bubbleList: List<Bubble> = emptyList()
        private lateinit var counter: Counter
        private var paint = Paint()

        private var isGameStopped = false

        init {
            paint.apply {
                color = Color.DKGRAY
                isAntiAlias = true
                isDither = true
                style = Paint.Style.FILL
            }

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            rootWidth = displayMetrics.widthPixels
            rootHeight = displayMetrics.heightPixels
            leftEdge = 0f + BUBBLE_RADIUS
            topEdge = 0f + BUBBLE_RADIUS
            rightEdge = rootWidth - BUBBLE_RADIUS
            val bottomNavBarHeight =
                resources.getDimension(
                    resources.getIdentifier(
                        "navigation_bar_height",
                        "dimen",
                        "android"
                    )
                )
            bottomEdge =
                rootHeight - BUBBLE_RADIUS - bottomNavBarHeight

            startNewGame()
        }


        private fun startNewGame() {
            val numberOfBubbles = Random.nextInt(1, 8)
            counter = Counter(numberOfBubbles * 2)
            isRunning = true
            isGameStopped = false
            bubbleList = getListOfBubbles(numberOfBubbles)
            resume()

            timer = object : CountDownTimer((numberOfBubbles * 2 * 1000 - 1000).toLong(), 950) {
                override fun onFinish() {
                    if(!checkWin()){
                        startDialog("Время вышло!")
                    }

                }

                override fun onTick(millisUntilFinished: Long) {
                    counter.nextSecond()
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
                    canvas.drawText(
                        counter.count.toString(),
                        0f,
                        BUBBLE_RADIUS,
                        counter.paint
                    )

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
                    if(checkWin()) startDialog("Победа!")
                }
                MotionEvent.ACTION_POINTER_DOWN -> {

                    val x = event.getX(event.actionIndex)
                    val y = event.getY(event.actionIndex)
                    checkTouchEvent(x, y, event.getPointerId(event.actionIndex))
                    if(checkWin()) startDialog("Победа!")
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

        private fun checkWin():Boolean {
            bubbleList.forEach {
                if (it.pointerId == null) return false
            }
            return true
        }

        private fun startDialog(string: String) {
            timer.cancel()
            isGameStopped = true
            pause()
            val builderDialog = AlertDialog.Builder(requireActivity())
            builderDialog.setTitle(string)
            builderDialog.setPositiveButton("Повторим!") { dialog, _ ->
                dialog.cancel()
                startNewGame()
            }
            builderDialog.setNegativeButton("Больше не хочу!") { dialog, _ ->
                requireActivity().onBackPressed()
                dialog.cancel()
            }
                .setCancelable(false)
            builderDialog.create().show()
        }

        private fun getListOfBubbles(count: Int): List<Bubble> {
            val list = mutableListOf<Bubble>()
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
            if(isGameStopped){
                for (bubble in bubbleList) {
                    bubble.pointerId = 99
                }
            }
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

        fun detach() {
            timer.cancel()
        }

    }
}
private fun Int.toRad() = this * PI / 180

