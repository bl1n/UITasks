package team.lf.uitasks.bubbles

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bubbles.*
import team.lf.uitasks.R
import kotlin.random.Random


class GeometryBubblesFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): GeometryBubblesFragment = GeometryBubblesFragment()

        const val BUBBLE_WIDTH_IN_DP = 100
        const val BUBBLE_HEIGHT_IN_DP = 100


    }

    private var bottom: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var left: Float = 0f
    private var rootHeight: Int = 0
    private var rootWidth: Int = 0
    private lateinit var timer: CountDownTimer
    private var numberOfBubbles = 0

    private var ivWidth = 0
    private var ivHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bubbles, container, false)
        numberOfBubbles = Random.nextInt(1, 7)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        ivWidth = (displayMetrics.density * BUBBLE_WIDTH_IN_DP + 0.5f).toInt()
        ivHeight = (displayMetrics.density * BUBBLE_HEIGHT_IN_DP + 0.5f).toInt()
        rootWidth = displayMetrics.widthPixels
        rootHeight = displayMetrics.heightPixels - 200

        left = 0f
        right= (rootWidth - ivWidth).toFloat()
        top = 0f
        bottom = (rootHeight - ivHeight).toFloat()

        addImView(root as ViewGroup)

        startCounter()
        return root
    }

    private fun addImView(root: ViewGroup) {
        val imageView = ImageView(root.context).apply {
            setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_brightness_1_black_24dp,
                    null
                )
            )
            layoutParams = ViewGroup.LayoutParams(ivWidth, ivHeight)

        }

        root.addView(imageView)
        addAnimator(imageView)
    }

    private fun addAnimator(imageView: View) {
        val pvhX = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_X,
            left
        )

        val pvhY =
            PropertyValuesHolder.ofFloat(
                View.TRANSLATION_Y,
                bottom

            )
        val animator = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX, pvhY)
        animator.duration = Random.nextLong(500, 2000)
        animator.start()
    }

    private fun startCounter() {
        timer = object : CountDownTimer((numberOfBubbles * 2 * 1000).toLong(), 1000) {
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
        if (count == numberOfBubbles) {
            timer.cancel()
            startDialog("Success!")
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

}