package team.lf.uitasks.bubbles

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bubbles.*
import team.lf.uitasks.ChooseFragment
import team.lf.uitasks.R
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


class GeometryBubblesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): GeometryBubblesFragment = GeometryBubblesFragment()

        const val TIMER_TICK_DURATION: Long = 50
    }

    private lateinit var countDownTimer: CountDownTimer
    private var numberOfBubbles: Int = 0
    private var countOfTouched = 0

    private var bottom: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var left: Float = 0f
    private var rootHeight: Int = 0
    private var rootWidth: Int = 0
    private var ivWidth = 0f
    private var ivHeight = 0f

    private var timerList = mutableListOf<Timer>()
    private var viewList = mutableListOf<ImageView>()

    private lateinit var root: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_bubbles, container, false) as ViewGroup
        initDimensions()
        startGame()
        return root
    }

    private fun startGame() {
        numberOfBubbles = Random.nextInt(1, 7)
        Log.d("TAG", "$numberOfBubbles")

        viewList.forEach {
            root.removeView(it)
        }
        for (i in 1..numberOfBubbles) {
            addImageView(root, ivWidth, ivHeight)
        }
        startCounter()
    }

    private fun checkBubbles(count: Int) {
        if (count == numberOfBubbles) {
            countDownTimer.cancel()
            startDialog("Ура!")
        }
    }

    private fun startCounter() {
        countDownTimer = object : CountDownTimer((numberOfBubbles * 2 * 1000).toLong(), 1000) {
            override fun onFinish() {
                startDialog("Время вышло!")
            }

            override fun onTick(millisUntilFinished: Long) {
                counter.text = if (millisUntilFinished >= 1000) {
                    millisUntilFinished.toInt().toString().subSequence(0, 1)
                } else {
                    "0"
                }
            }
        }
        countDownTimer.start()
    }

    private fun initDimensions() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        ivWidth = resources.getDimension(R.dimen.width)
        ivHeight = resources.getDimension(R.dimen.height)
        rootWidth = displayMetrics.widthPixels
        rootHeight = displayMetrics.heightPixels - 200
        right = rootWidth - ivWidth
        bottom = rootHeight - ivHeight
    }

    private fun addImageView(
        viewGroup: ViewGroup,
        ivWidth: Float,
        ivHeight: Float
    ) {
        val imageView = ImageView(viewGroup.context).apply {
            setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_brightness_1_black_24dp,
                    null
                )
            )
            layoutParams = LinearLayout.LayoutParams(ivWidth.toInt(), ivHeight.toInt())

        }

        imageView.setOnClickListener {
            it.animation
        }
        viewGroup.addView(imageView)
        viewList.add(imageView)
        startTimer(imageView)
    }

    private fun startTimer(view: View) {
        val timer = Timer()
        timerList.add(timer)
        val currentAngle = Random.nextInt(1, 360).toRad().toFloat()
        view.x = 0f
        view.y = 0f
        val c = Random.nextInt(80, 150)
        var deltaX = c * cos(currentAngle)
        var deltaY = c * sin(currentAngle)


        var task = MyTimerTask(view, deltaX, deltaY)
        timer.schedule(
            task,
            0,
            100
        )

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    deltaX = task.deltaX
                    deltaY = task.deltaY
                    task.cancel()
                    countOfTouched++
                    checkBubbles(countOfTouched)
                }
                MotionEvent.ACTION_UP -> {
                    task = MyTimerTask(view, deltaX, deltaY)
                    timer.schedule(
                        task,
                        0,
                        TIMER_TICK_DURATION
                    )
                    countOfTouched--
                }
            }
            false
        }
    }

    fun startDialog(string: String) {
        val builderDialog = AlertDialog.Builder(requireActivity())
        builderDialog.setTitle(string)
        builderDialog.setPositiveButton("Повторим!") { dialog, _ ->
            dialog.cancel()
            startGame()

        }
        builderDialog.setNegativeButton("Больше не хочу!") { dialog, _ ->
            countDownTimer.cancel()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChooseFragment.newInstance())
                .commit()
            dialog.cancel()
        }
            .setCancelable(false)
        builderDialog.create().show()
    }

    inner class MyTimerTask(
        private val view: View,
        var deltaX: Float,
        var deltaY: Float
    ) : TimerTask() {

        override fun run() {
            when {
                view.y + deltaY / 2 >= bottom -> {
                    deltaY = -deltaY
                }
                view.y + deltaY / 2 <= top -> {
                    deltaY = -deltaY
                }
                view.x + deltaX / 2 <= left -> {
                    deltaX = -deltaX
                }
                view.x + deltaX / 2 >= right -> {
                    deltaX = -deltaX
                }
            }
            Handler(Looper.getMainLooper()).post {
                val animator = ObjectAnimator.ofPropertyValuesHolder(
                    view,
                    PropertyValuesHolder.ofFloat(View.X, view.x + deltaX),
                    PropertyValuesHolder.ofFloat(View.Y, view.y + deltaY)
                ).apply {
                    interpolator = LinearInterpolator()
                    duration = TIMER_TICK_DURATION
                }
                animator
                    .start()

            }
        }

    }
    override fun onPause() {
        countDownTimer.cancel()
        timerList.forEach {
            it.cancel()
        }
        super.onPause()

    }

    private fun Int.toRad() = this * PI / 180
}





