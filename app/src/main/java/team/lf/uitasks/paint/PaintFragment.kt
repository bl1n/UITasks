package team.lf.uitasks.paint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import team.lf.uitasks.R

class PaintFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_paint, container, false)

        return view
    }

    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(): PaintFragment = PaintFragment()
    }
}