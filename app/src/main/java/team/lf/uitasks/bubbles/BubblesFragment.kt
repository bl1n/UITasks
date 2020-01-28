package team.lf.uitasks.bubbles

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addPauseListener
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

const val IMAGE_VIEW_WIDTH_IN_DP = 100
const val IMAGE_VIEW_HEIGHT_IN_DP = 100

class BubblesFragment : Fragment() {

    lateinit var displayMetrics: DisplayMetrics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val root = inflater.inflate(R.layout.fragment_bubbles, container, false)
        val rootWidth = displayMetrics.widthPixels /displayMetrics.density
        val rootHeight = displayMetrics.heightPixels
        val ivWidth = (displayMetrics.density * IMAGE_VIEW_WIDTH_IN_DP + 0.5f).toInt()
        val ivHeight = (displayMetrics.density * IMAGE_VIEW_HEIGHT_IN_DP + 0.5f).toInt()
        (root as ConstraintLayout).addImageView(ivWidth, ivHeight, rootWidth.toInt(), rootHeight)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(): BubblesFragment = BubblesFragment()
    }
}

private fun ConstraintLayout.addImageView(
    ivWidth: Int,
    ivHeight: Int,
    rootWidth: Int,
    rootHeight: Int
) {

    val imageView = ImageView(context).apply {
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
    imageView.setAnimator(rootWidth, rootHeight, ivWidth, ivHeight)
    addView(imageView)
}

@SuppressLint("ClickableViewAccessibility")
private fun ImageView.setAnimator(
    rootWidth: Int,
    rootHeight: Int,
    ivWidth: Int,
    ivHeight: Int
) {
    Log.d("TAG", "$rootWidth $rootHeight $ivHeight $ivWidth")
    val pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, (rootWidth-ivWidth).toFloat())
    val pvhY =
        PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, (rootHeight - ivHeight).toFloat())
    val animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY)
    animator.duration = 4000

    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onAnimationEnd(animation: Animator?) {
            this@setAnimator.setAnimator(rootWidth, rootHeight, ivWidth, ivHeight)
        }

        override fun onAnimationCancel(animation: Animator?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    })
    animator.start()


    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> animator.pause()
            MotionEvent.ACTION_UP -> animator.resume()
        }
        true
    }
}