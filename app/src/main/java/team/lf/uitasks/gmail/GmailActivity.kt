package team.lf.uitasks.gmail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import team.lf.uitasks.R

class GmailActivity : AppCompatActivity() {

    private lateinit var avatarImageView: ImageView
    private lateinit var hideableViewGroup: ViewGroup
    private lateinit var cardView: CardView
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var cardConctraitLayout: ConstraintLayout
    private lateinit var googleStart: TextView
    private lateinit var googleEnd: TextView
    private lateinit var fakeToolbar: View
    private lateinit var loremTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail)

        avatarImageView = findViewById(R.id.avatarImageView)
        hideableViewGroup = findViewById(R.id.hideble_view_group)
        cardView = findViewById(R.id.cardView)
        bottomSheet = findViewById(R.id.bottom_sheet)
        googleStart = findViewById(R.id.google_start)
        googleEnd = findViewById(R.id.google_end)
        fakeToolbar = findViewById(R.id.fake_toolbar)
        loremTv = findViewById(R.id.lorem_tv)
        cardConctraitLayout = findViewById(R.id.card_constrait_layout)

        val shadowBox = findViewById<ViewGroup>(R.id.shadowBox)

        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    shadowBox.visibility = View.VISIBLE
                    window.statusBarColor = Color.DKGRAY
                }
            }

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                changeImageSize(slideOffset)
                setButtonsVisibility(slideOffset)
                moveCardView(slideOffset)
                changeStatusBarColor(slideOffset)
                setStatusBarIconColor(slideOffset)
                changeCornerRadius(slideOffset)
                moveAvatar(slideOffset)
                setGoogleVisibility(slideOffset)
                setToolbar(slideOffset)
                moveLorem(slideOffset)
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowBox.visibility = View.GONE
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window.statusBarColor =
                                resources.getColor(R.color.colorPrimaryDark, null)
                        } else
                            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)

                    }
                }
            }
        })
    }

    private fun moveLorem(slideOffset: Float) {
        val margin = when {
            slideOffset in 0.1f..0.9f -> {
                (resources.getDimensionPixelSize(R.dimen.lorem_margin) * (1 - slideOffset)).toInt()
            }
            slideOffset > 0.6f -> {
                0
            }
            else -> {
                resources.getDimensionPixelSize(R.dimen.lorem_margin)
            }
        }
        val set = ConstraintSet()
        set.clone(cardConctraitLayout)
        set.connect(
            loremTv.id,
            ConstraintSet.TOP,
            cardConctraitLayout.id,
            ConstraintSet.TOP,
            margin + resources.getDimensionPixelSize(R.dimen.card_margin_top) + +resources.getDimensionPixelSize(
                R.dimen.end_lorem_margin
            )
        )
        set.applyTo(cardConctraitLayout)
    }

    private fun moveCardView(slideOffset: Float) {
        val margin = when {
            slideOffset in 0.1f..0.9f -> {
                (resources.getDimensionPixelSize(R.dimen.card_margin_top) * (1 - slideOffset)).toInt()
            }
            slideOffset > 0.6f -> {
                0
            }
            else -> {
                resources.getDimensionPixelSize(R.dimen.card_margin_top)
            }
        }
        val set = ConstraintSet()
        set.clone(bottomSheet)
        set.connect(cardView.id, ConstraintSet.TOP, bottomSheet.id, ConstraintSet.TOP, margin)
        set.applyTo(bottomSheet)
    }

    private fun setStatusBarIconColor(slideOffset: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            if (slideOffset in 0.99f..1f) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }
        }
    }

    private fun setToolbar(slideOffset: Float) {
        if (slideOffset == 1f) {
            fakeToolbar.alpha = 0.9f
        } else {
            fakeToolbar.alpha = 0f
        }
    }

    private fun setGoogleVisibility(slideOffset: Float) {

        if (slideOffset in 0.9f..1f) {
            googleEnd.alpha = 0.3f + (slideOffset% 0.9f) / 5
        } else {
            googleEnd.alpha = 0f
        }
        if(slideOffset == 1f){
            googleEnd.alpha = 1f
        }
        Log.d("TAG", "${googleEnd.alpha}")
    }

    private fun moveAvatar(slideOffset: Float) {
        val bias = when (slideOffset) {
            in 0.5f..1f -> {
                0.5f - (-0.5f + slideOffset)
            }
            else -> 0.5f

        }
        val set = ConstraintSet()
        set.clone(bottomSheet)
        set.setHorizontalBias(avatarImageView.id, bias)
        set.applyTo(bottomSheet)
    }

    private fun changeCornerRadius(slideOffset: Float) {

        val radius = when (slideOffset) {
            in 0.90f..0.98f -> {
                resources.getDimension(R.dimen.card_corner_radius) / 2
            }
            in 0.98f..1f -> {
                0f
            }
            else -> {
                resources.getDimension(R.dimen.card_corner_radius)
            }
        }
        cardView.radius = radius
    }

    private fun changeStatusBarColor(slideOffset: Float) {
        when (slideOffset) {
            in 0.99f..1f -> setStatusBarColor(Color.WHITE)
            else -> setStatusBarColor(Color.DKGRAY)
        }
    }

    private fun setButtonsVisibility(slideOffset: Float) {
        if (slideOffset in 0.001f..1f) {
            googleStart.alpha = 0.5f - slideOffset
            hideableViewGroup.alpha = 0.5f - slideOffset
        } else {
            googleStart.alpha = 1f
            hideableViewGroup.alpha = 1f
        }
    }

    private fun changeImageSize(slideOffset: Float) {

        val diameter = if (slideOffset in 0.1f..1f) {
            (resources.getDimensionPixelSize(R.dimen.diameter) / (slideOffset.toDouble() + 1)).toInt()
        } else {
            resources.getDimensionPixelSize(R.dimen.diameter)
        }
        avatarImageView.layoutParams.width = diameter
        avatarImageView.layoutParams.height = diameter
        avatarImageView.requestLayout()


    }

    private fun setStatusBarColor(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

}