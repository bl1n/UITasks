package team.lf.uitasks.gmail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import de.hdodenhof.circleimageview.CircleImageView
import team.lf.uitasks.R

class GmailActivity : AppCompatActivity() {

    private lateinit var avatarImageView: CircleImageView
    private lateinit var hideableViewGroup: ViewGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar)
        setContentView(R.layout.activity_gmail)

        avatarImageView = findViewById(R.id.avatarImageView)
        hideableViewGroup = findViewById(R.id.hideble_view_group)

        val shadowBox = findViewById<ViewGroup>(R.id.shadowBox)
        val bottomSheet = findViewById<ViewGroup>(R.id.bottom_sheet)
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    shadowBox.visibility = View.VISIBLE
                }
            }

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                changeImage(slideOffset)
                setButtonsVisibility(slideOffset)


            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowBox.visibility = View.GONE
                    }

                }
            }

        })
    }

    //todo change to alpha animation
    private fun setButtonsVisibility(slideOffset: Float) {
        if (slideOffset >= 0.5) {
            hideableViewGroup.visibility = View.GONE
        } else {
            hideableViewGroup.visibility = View.VISIBLE

        }
    }

    private fun changeImage(slideOffset: Float) {

        val diameter = if (slideOffset in 0.1f..1f) {
            (resources.getDimensionPixelSize(R.dimen.diameter) / (slideOffset.toDouble() + 1)).toInt()
        } else {
            resources.getDimensionPixelSize(R.dimen.diameter)
        }
        avatarImageView.layoutParams.width = diameter
        avatarImageView.layoutParams.height = diameter
        avatarImageView.requestLayout()


    }

}