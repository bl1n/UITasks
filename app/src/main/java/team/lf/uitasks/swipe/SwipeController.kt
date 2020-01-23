package team.lf.uitasks.swipe

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

enum class ButtonsState {
    GONE,
    RIGHT_VISIBLE,
}

class SwipeController(val actions:SwipeControllerActions) : Callback() {

    private var swipeBack: Boolean = false
    private var buttonShowedState = ButtonsState.GONE
    private val buttonsWidth = 600f
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.d("TAG", "onChildDraw")
        var x = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                x = dX.coerceAtMost(-buttonsWidth)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    x,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    x,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, x, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.d("TAG", "setTouchListener")

        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL ||
                        event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonsWidth) {
                    buttonShowedState = ButtonsState.RIGHT_VISIBLE
                }
                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            return@setOnTouchListener false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        currentlyActive: Boolean
    ) {
        Log.d("TAG", "setTouchDownListener")

        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    currentlyActive
                )
            }
            return@setOnTouchListener false
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.d("TAG", "setTouchUpListener")
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0F,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { _, _ ->
                    return@setOnTouchListener false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                buttonShowedState = ButtonsState.GONE
                currentItemViewHolder = null
            }
            return@setOnTouchListener false
        }
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView, isClickable
        : Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPAdding: Float = buttonsWidth / 2 - 20
        val corners = 16f

        val itemView = viewHolder.itemView
        val p = Paint()

        val blueButton = RectF(
            itemView.right - buttonWidthWithoutPAdding, itemView.top.toFloat(),
            itemView.right.toFloat(), itemView.bottom.toFloat()
        )
        p.color = Color.BLUE
        c.drawRoundRect(blueButton, corners, corners, p)

        val redButton = RectF(
            itemView.right - (buttonWidthWithoutPAdding * 2 + 20), itemView.top.toFloat(),
            itemView.right - (buttonWidthWithoutPAdding + 20), itemView.bottom.toFloat()
        )
        p.color = Color.RED
        c.drawRoundRect(redButton, corners, corners, p)
        drawText("BLUE", c, blueButton, p)
        p.color = Color.BLUE
        drawText("RED", c, redButton, p)


    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) {
        val textSize = 60f
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {
            drawButtons(c, it)
        }
    }

}