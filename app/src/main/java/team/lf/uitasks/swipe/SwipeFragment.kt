package team.lf.uitasks.swipe

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
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
        val swipeController = SwipeController()
        val itemTouchHelper = ItemTouchHelper(swipeController)

        val view = inflater.inflate(R.layout.fragment_swipe, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = swipeAdapter
        }
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
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