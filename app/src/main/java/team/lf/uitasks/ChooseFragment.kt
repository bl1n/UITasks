package team.lf.uitasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import team.lf.uitasks.bubbles.BubblesFragment
import team.lf.uitasks.bubbles.GeometryBubblesFragment
import team.lf.uitasks.paint.PaintFragment
import team.lf.uitasks.swipe.SwipeFragment

class ChooseFragment :Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose, container, false)
        setButtons(view)
        return  view
    }

    private fun setButtons(view: View) {
        view.findViewById<Button>(R.id.buttonSwipe).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SwipeFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.buttonPaint).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, PaintFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<Button>(R.id.buttonBubbles).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SurfaceViewBubblesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

    }
    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(): ChooseFragment = ChooseFragment()
    }
}