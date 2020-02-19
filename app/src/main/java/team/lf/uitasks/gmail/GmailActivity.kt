package team.lf.uitasks.gmail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import team.lf.uitasks.R

class GmailActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar)
        setContentView(R.layout.activity_gmail)

        val shadowBox = findViewById<ViewGroup>(R.id.shadowBox)
        val bottomSheet = findViewById<ViewGroup>(R.id.bottom_sheet)
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    shadowBox.visibility = View.VISIBLE
                }
            }

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_HIDDEN->{
                        shadowBox.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_DRAGGING->{

                    }

                }
            }

        })
    }

}