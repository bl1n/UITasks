package team.lf.uitasks.gmail

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import team.lf.uitasks.R
import team.lf.uitasks.swipe.SwipeAdapter
import team.lf.uitasks.swipe.SwipeController
import team.lf.uitasks.swipe.SwipeControllerActions

class GmailV2Activity : AppCompatActivity() {

    private lateinit var bottomSheetV2: CoordinatorLayout

    private lateinit var swipeAdapter:SwipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail_v2)
        bottomSheetV2 =findViewById(R.id.bottom_sheet_v2)

        val shadowBox = findViewById<ViewGroup>(R.id.shadowBox)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_gmailv2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initRecycler()

        val sheetBehavior = BottomSheetBehavior.from(bottomSheetV2)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        findViewById<Button>(R.id.bottom_sheet_button)
            .setOnClickListener {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    window.statusBarColor = Color.DKGRAY
                    shadowBox.visibility = View.VISIBLE
                }
            }

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowBox.visibility = View.GONE
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark, null)
                        } else
                            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
                    }
                }
            }
        })
    }

    private fun initRecycler() {

        swipeAdapter = SwipeAdapter()
        val swipeController = SwipeController(object : SwipeControllerActions {
            override fun onRedButtonClicked(position: Int) {
                Toast.makeText(
                    this@GmailV2Activity,
                    "Red button is clicked on $position position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onBlueButtonClicked(position: Int) {
                Toast.makeText(
                    this@GmailV2Activity,
                    "Blue button is clicked on $position position",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        val itemTouchHelper = ItemTouchHelper(swipeController)

        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = swipeAdapter
        }
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
        swipeAdapter.addItems(SwipeAdapter.populateUsers())

    }

}
