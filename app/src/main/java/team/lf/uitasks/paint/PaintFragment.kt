package team.lf.uitasks.paint

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import team.lf.uitasks.R

class PaintFragment : Fragment() {

    lateinit var paintWithPaletteView: PaintWithPaletteView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_paint, container, false)

        paintWithPaletteView = view.findViewById(R.id.paintWithPalette)

        return view
    }

    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(): PaintFragment = PaintFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.undo -> {
//                Toast.makeText(context, "Undo", Toast.LENGTH_SHORT).show()
//                paintView.undoStep()
//            }
//            R.id.redo -> {
//                Toast.makeText(context, "Redo", Toast.LENGTH_SHORT).show()
//                paintView.redoStep()
//
//            }
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.draw_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}