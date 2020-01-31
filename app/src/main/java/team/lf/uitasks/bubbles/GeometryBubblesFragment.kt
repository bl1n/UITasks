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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginLeft
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
    }

    private var numberOfBubbles = 0

    private var bottom: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var left: Float = 0f
    private var rootHeight: Int = 0
    private var rootWidth: Int = 0
    private var ivWidth = 0
    private var ivHeight = 0
    private var timerList = mutableListOf<Timer>()


    private lateinit var listOfAngles: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bubbles, container, false)
        numberOfBubbles = Random.nextInt(1, 7)
        val time = numberOfBubbles * 2

        initDimensions()

        populateListOfAngles()
        addImageView(root as ViewGroup, ivWidth, ivHeight)
        addImageView(root as ViewGroup, ivWidth, ivHeight)

        return root
    }

    private fun addImageView(
        viewGroup: ViewGroup,
        ivWidth: Int,
        ivHeight: Int
    ) {
        val imageView = ImageView(viewGroup.context).apply {
            setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_brightness_1_black_24dp,
                    null
                )
            )
            layoutParams = ViewGroup.LayoutParams(ivWidth, ivHeight)
        }

        imageView.setOnClickListener {
            it.animation
        }
        viewGroup.addView(imageView)
        startTimer(imageView, getAngle())
    }

    private fun getAngle(): Int  = listOfAngles[Random.nextInt(listOfAngles.size)]

    fun toRad(grad: Int) = grad * PI / 180

    private fun startTimer(view: View, initAngle: Int) {
        val timer = Timer()
        timerList.add(timer)
        var currentAngle = initAngle
        var gipo = 50

        (view.layoutParams as LinearLayout.LayoutParams).marginStart =Random.nextInt(0, right.toInt())
        (view.layoutParams as LinearLayout.LayoutParams).topMargin = Random.nextInt(0, bottom.toInt())

        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (view.left.toFloat() == left) {
                        currentAngle =- currentAngle
                    } else if (view.right.toFloat() == right) {
                        currentAngle =- currentAngle

                    } else if (view.top.toFloat() == top) {
                        currentAngle =- currentAngle

                    } else if (view.bottom.toFloat() == bottom) {
                        currentAngle =- currentAngle

                    }

                    gipo += 50
                    val nextX = gipo * cos(currentAngle.toFloat())
                    val nextY = gipo * sin (currentAngle.toFloat())
                    Log.d("TAG", "in timer $nextX, $nextY")
                    Handler(Looper.getMainLooper()).post{
                        ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat( View.TRANSLATION_X, nextX),
                            PropertyValuesHolder.ofFloat( View.TRANSLATION_Y, nextY)
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

    private fun populateListOfAngles() {
        listOfAngles = mutableListOf()
        for (i in 0..360 step 45) {
            Log.d("TAG", "$i")
            listOfAngles.add(i)
        }
    }

    private fun initDimensions() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        ivWidth = (displayMetrics.density * BUBBLE_WIDTH_IN_DP + 0.5f).toInt()
        ivHeight = (displayMetrics.density * BUBBLE_HEIGHT_IN_DP + 0.5f).toInt()
        rootWidth = displayMetrics.widthPixels
        rootHeight = displayMetrics.heightPixels - 200

        left = 0f
        right = (rootWidth - ivWidth).toFloat()
        top = 0f
        bottom = (rootHeight - ivHeight).toFloat()


    }

    override fun onPause() {
        super.onPause()
        timerList.forEach {
            it.cancel()
        }
    }


}
