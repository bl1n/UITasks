package team.lf.uitasks.bubbles

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
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

        const val BUBBLE_WIDTH_IN_DP = 100
        const val BUBBLE_HEIGHT_IN_DP = 100
        const val SPEED = 30
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
        addImageView(root as ViewGroup, ivWidth, ivHeight)
//        addImageView(root as ViewGroup, ivWidth, ivHeight)

        return root
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
        view.x = Random.nextInt(0, right.toInt()).toFloat()
        view.y = Random.nextInt(0, bottom.toInt()).toFloat()
        val c = 50
        var deltaX = c * cos(currentAngle)
        var deltaY = c * sin(currentAngle)

        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    Log.d("TAG", "x = ${view.x} y =  ${view.y}")
//                    Log.d("TAG", "$deltaX, $deltaY")

                    if (view.y >= bottom || view.y <= top) {
                        deltaY = -deltaY
                    }
                    if (view.x <= left || view.x >= right) {
                        deltaX = -deltaX
                    }



                    Handler(Looper.getMainLooper()).post {
                        ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat(View.X, view.x + deltaX),
                            PropertyValuesHolder.ofFloat(View.Y, view.y + deltaY)
                        ).apply {
                            interpolator = LinearInterpolator()
                            duration = 100
                        }
                            .start()
                    }

                }
            },
            0,
            100
        )

    }


    override fun onPause() {
        super.onPause()
        timerList.forEach {
            it.cancel()
        }
    }

}

