package team.lf.uitasks.bubbles

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import team.lf.uitasks.R

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
        val rootWidth = (displayMetrics.widthPixels * displayMetrics.density + 0.5f)
        val rootHeight = (displayMetrics.heightPixels * displayMetrics.density + 0.5f)
        val ivWidth = (displayMetrics.density * IMAGE_VIEW_WIDTH_IN_DP + 0.5f).toInt()
        val ivHeight = (displayMetrics.density * IMAGE_VIEW_HEIGHT_IN_DP + 0.5f).toInt()
        (root as ConstraintLayout).addImageView(ivWidth, ivHeight, rootWidth, rootHeight)
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
    rootWidth: Float,
    rootHeight: Float
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
    imageView.setAnimator(rootWidth, rootHeight, ivWidth, ivHeight)
    addView(imageView)
}

private fun ImageView.setAnimator(
    rootWidth: Float,
    rootHeight: Float,
    ivWidth: Int,
    ivHeight: Int
) {

    Log.d("TAG", "$rootHeight $rootWidth $ivHeight $ivWidth")
        val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, (rootWidth-ivWidth/2))
        animator.start()
}