package team.lf.uitasks.gmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.lf.uitasks.R

class ContactDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): ContactDetailsBottomSheetDialogFragment =
            ContactDetailsBottomSheetDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.bottom_sheet, container, false)
    }
}