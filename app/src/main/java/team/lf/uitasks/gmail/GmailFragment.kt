package team.lf.uitasks.gmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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


        view.findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                val fragment = ContactDetailsBottomSheetDialogFragment.newInstance()
                fragment.apply {

                }.show(requireActivity().supportFragmentManager, null)
            }
        return view
    }
}