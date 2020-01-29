package team.lf.uitasks.bubbles

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bubbles.*
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

    private lateinit var timer: CountDownTimer
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
        val ivWidth = (displayMetrics.density * BUBBLE_WIDTH_IN_DP + 0.5f).toInt()
        val ivHeight = (displayMetrics.density * BUBBLE_HEIGHT_IN_DP + 0.5f).toInt()
        val rootWidth = displayMetrics.widthPixels
        val rootHeight = displayMetrics.heightPixels - 200

        listOfX = listOf(0+ivWidth/2, rootWidth / 3, rootWidth / 2, rootWidth / 3 * 2, rootWidth-ivWidth/2)
        listOfY = listOf(0+ivHeight/2, rootHeight / 3, rootHeight / 2, rootHeight / 3 * 2, rootHeight-ivHeight/2)

        for (i in 0 until NUMBER_OF_BUBBLES)
            addImageView((root as ConstraintLayout), ivWidth, ivHeight)

        startCounter()
        return root
    }

    private fun startCounter() {
        timer = object : CountDownTimer((NUMBER_OF_BUBBLES * 2 * 1000).toLong(), 1000) {
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
        timer.start()
    }

    private fun checkBubbles(count: Int) {
        if (count == NUMBER_OF_BUBBLES) {
            timer.cancel()
            startDialog("Success!")
        }
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
        setAnimatorToIv(imageView)
        constraintLayout.addView(imageView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAnimatorToIv(
        imageView: ImageView
    ) {

        Log.d("TAG", "setAnimatorToIv")
        val ivWidth = imageView.layoutParams.width
        val ivHeight = imageView.layoutParams.height
        val nextX = listOfX[Random.nextInt(listOfX.size)] - ivWidth / 2
        val nextY = listOfY[Random.nextInt(listOfY.size)] - ivHeight / 2
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
        animator.duration = Random.nextLong(1000, 2000)

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                setAnimatorToIv(imageView)
                animation?.cancel()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()

        imageView.setOnTouchListener { _, event ->
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

    fun startDialog(string: String) {
        val builderDialog = AlertDialog.Builder(requireActivity())
        builderDialog.setTitle(string)
        builderDialog.setPositiveButton("Повторим!") { dialog, which ->
            startCounter()
            dialog.cancel()
        }
        builderDialog.setNegativeButton("Больше не хочу!") { dialog, which ->
            requireActivity().onBackPressed()
            dialog.cancel()
        }
        builderDialog.create().show()
    }

    override fun onDetach() {
        timer.cancel()
        super.onDetach()
    }

}





