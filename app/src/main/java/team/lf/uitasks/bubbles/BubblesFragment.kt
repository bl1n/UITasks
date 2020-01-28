package team.lf.uitasks.bubbles

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import team.lf.uitasks.R
import kotlin.random.Random

/**
 * 3) Игра пузырьки. На экране рисуется от 3 до 10 окружностей.
 * Окружности двигаются и отталкиваются от рамок экрана.
 * Пользователю дается количество окружностей * 2 секунд для того, чтобы поставить пальцы на все окружности.
 * Если палец на окружности она не двигается.
 * Если пользователь поставит палец, а затем уберет его с оружности то окружность снова начнет двигаться.
 * */

const val BUBBLE_WIDTH_IN_DP = 100
const val BUBBLE_HEIGHT_IN_DP = 100
const val NUMBER_OF_BUBBLES = 3

class BubblesFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): BubblesFragment = BubblesFragment()
    }
    private var countOfTouched = 0

    private lateinit var listOfX: List<Int>
    private lateinit var listOfY: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bubbles, container, false)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val rootWidth = displayMetrics.widthPixels
        val rootHeight = displayMetrics.heightPixels
        val ivWidth = (displayMetrics.density * BUBBLE_WIDTH_IN_DP + 0.5f).toInt()
        val ivHeight = (displayMetrics.density * BUBBLE_HEIGHT_IN_DP + 0.5f).toInt()

        listOfX = listOf(0, rootWidth / 2, rootWidth)
        listOfY = listOf(0, rootHeight / 2, rootHeight)

        for (i in 0 until NUMBER_OF_BUBBLES)
            addImageView((root as ConstraintLayout), ivWidth, ivHeight)
        return root
    }

    private fun checkBubbles(count: Int) {
        if (count == NUMBER_OF_BUBBLES)
            Toast.makeText(
                context,
                "CAUGHT!",
                Toast.LENGTH_SHORT
            ).show() //todo add dialog with("Success!")
    }

    private fun addImageView(
        constraintLayout: ConstraintLayout,
        ivWidth: Int,
        ivHeight: Int
    ) {

        val imageView = ImageView(constraintLayout.context).apply {
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
        setAnimator(imageView, listOfX, listOfY)
        constraintLayout.addView(imageView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAnimator(
        imageView: ImageView,
        listOfX: List<Int>,
        listOfY: List<Int>
    ) {

        val nextX = listOfX[Random.nextInt(listOfX.size)]
        val nextY = listOfY[Random.nextInt(listOfY.size)]
        Log.d("TAG", "$nextX $nextY ")
        val pvhX = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_X,
            nextX.toFloat()
        )
        val pvhY =
            PropertyValuesHolder.ofFloat(
                View.TRANSLATION_Y,
                nextY.toFloat()
            )
        val animator = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX, pvhY)
        animator.duration = 2000

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                setAnimator(imageView, listOfX, listOfY)
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()

        imageView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animator.pause()
                    countOfTouched++
                    checkBubbles(countOfTouched)
                }
                MotionEvent.ACTION_UP -> {
                    animator.resume()
                    countOfTouched--
                }
            }
            false
        }
    }

}





