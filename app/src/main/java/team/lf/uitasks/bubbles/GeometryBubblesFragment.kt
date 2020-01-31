package team.lf.uitasks.bubbles

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
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
    }


    private var bottom: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var left: Float = 0f
    private var rootHeight: Int = 0
    private var rootWidth: Int = 0
    private var ivWidth = 0f
    private var ivHeight = 0f
    private var timerList = mutableListOf<Timer>()


    private lateinit var listOfAngles: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bubbles, container, false)
        initDimensions()

        populateListOfAngles()

        startGame(root as ViewGroup)
//        addImageView(root as ViewGroup, ivWidth, ivHeight)
//        addImageView(root as ViewGroup, ivWidth, ivHeight)

        return root
    }

    private fun startGame(root: ViewGroup) {
        val numberOfBubbles = Random.nextInt(1, 7)
        for (i in 1..numberOfBubbles) {
            addImageView(root, ivWidth, ivHeight)
        }
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

    private fun populateListOfAngles() {
        listOfAngles = mutableListOf()
        for (i in 0..360 step 45) {
            listOfAngles.add(i)
        }
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
        startTimer(imageView)
    }

    private fun getAngle(): Int = listOfAngles[Random.nextInt(listOfAngles.size)]

    private fun Int.toRad() = this * PI / 180

    private fun startTimer(view: View) {
        val timer = Timer()
        timerList.add(timer)
        var currentAngle = getAngle().toRad().toFloat()
        view.x = Random.nextInt(0, (right - ivWidth).toInt()).toFloat()
        view.y = Random.nextInt(0, (bottom - ivHeight).toInt()).toFloat()
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
                }
                MotionEvent.ACTION_UP -> {
                    task = MyTimerTask(view, deltaX, deltaY)
                    timer.schedule(
                        task,
                        0,
                        100
                    )
                }
            }
            false
        }
    }


    override fun onPause() {
        super.onPause()
        timerList.forEach {
            it.cancel()
        }
    }

    inner class MyTimerTask(
        private val view: View,
        var deltaX: Float,
        var deltaY: Float
    ) : TimerTask() {

        override fun run() {
            if (view.y + deltaY / 2 >= bottom || view.y + deltaY / 2 <= top) {
                deltaY = -deltaY
            }
            if (view.x + deltaX / 2 <= left || view.x + deltaX / 2 >= right) {
                deltaX = -deltaX
            }
            Handler(Looper.getMainLooper()).post {
                val animator = ObjectAnimator.ofPropertyValuesHolder(
                    view,
                    PropertyValuesHolder.ofFloat(View.X, view.x + deltaX),
                    PropertyValuesHolder.ofFloat(View.Y, view.y + deltaY)
                ).apply {
                    interpolator = LinearInterpolator()
                    duration = 100
                }
                animator
                    .start()


            }
        }
    }
}





