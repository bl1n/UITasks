package team.lf.uitasks.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import team.lf.uitasks.R

class SwipeFragment : Fragment() {

    lateinit var swipeAdapter: SwipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        swipeAdapter = SwipeAdapter()
        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = swipeAdapter
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeAdapter.addItems(SwipeAdapter.populateUsers())
    }

    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(): SwipeFragment = SwipeFragment()
    }
}