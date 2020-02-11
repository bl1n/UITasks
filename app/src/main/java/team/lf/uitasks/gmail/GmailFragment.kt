package team.lf.uitasks.gmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return layoutInflater.inflate(R.layout.fragment_gmail, container,false)
    }
}