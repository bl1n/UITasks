package team.lf.uitasks.paint

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import team.lf.uitasks.R

class PaintWithPaletteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var paintView: PaintView
    private lateinit var firstColor:ImageView
    private lateinit var secondColor:ImageView

    init {
        inflate(context, R.layout.view_paint_with_palette, this)
        firstColor = findViewById(R.id.first)
        firstColor.setOnClickListener {
            Log.d("TAG", "first")
        }
        secondColor = findViewById(R.id.second)
        secondColor.setOnClickListener {
            Log.d("TAG", "second")
        }

    }
}