package team.lf.uitasks.gmail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(R.layout.activity_gmail)

        avatarImageView = findViewById(R.id.avatarImageView)
        hideableViewGroup = findViewById(R.id.hideble_view_group)
        cardView = findViewById(R.id.cardView)
        bottomSheet = findViewById(R.id.bottom_sheet)

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
                changeCornerRadius(slideOffset)
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowBox.visibility = View.GONE
                        window.statusBarColor = Color.GRAY
                    }
                }
            }
        })
    }

    private fun changeCornerRadius(slideOffset: Float) {

        val radius = when(slideOffset){
            in 0.90f..0.98f-> {
                resources.getDimension(R.dimen.card_corner_radius)/2
            }
            in 0.98f..1f->{
                0f
            }
            else -> {
                resources.getDimension(R.dimen.card_corner_radius)
            }
        }
        cardView.radius = radius
    }

    private fun changeStatusBarColor(slideOffset: Float) {
        when(slideOffset){
            in 0.99f..1f-> setStatusBarColor(Color.WHITE)
            else -> setStatusBarColor(Color.DKGRAY)
        }
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

    private fun setButtonsVisibility(slideOffset: Float) {
        if (slideOffset >= 0.5) {
            hideableViewGroup.visibility = View.GONE
        } else {
            hideableViewGroup.visibility = View.VISIBLE

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

    private fun setStatusBarColor(color:Int){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

}