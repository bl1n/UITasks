package team.lf.uitasks.gmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import team.lf.uitasks.R

class GmailFragment:Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): GmailFragment =
            GmailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = layoutInflater.inflate(R.layout.fragment_gmail, container,false)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val bottomSheet  = view.findViewById<ViewGroup>(R.id.bottom_sheet)
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        view.findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                if(sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN){
                    sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                } else {
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        return view
    }
}