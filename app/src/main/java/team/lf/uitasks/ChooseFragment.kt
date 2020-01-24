package team.lf.uitasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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

    }
    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(): ChooseFragment = ChooseFragment()
    }
}