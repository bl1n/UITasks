package team.lf.uitasks.paint

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat.getColor
import team.lf.uitasks.R

class PaintWithPaletteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val paintView: PaintView
    private var firstColorImageView: ImageView
    private var secondColorImageView: ImageView
    private var thirdColorImageView: ImageView
    private var fourthColorImageView: ImageView
    private var fifthColorImageView: ImageView

//    private var firstColor = getColor(resources, R.color.red, null)
//    private var secondColor = getColor(resources, R.color.green, null)
//    private var thirdColor = getColor(resources, R.color.blue, null)
//    private var fourthColor = getColor(resources, R.color.black, null)
//    private var fifthColor = getColor(resources, R.color.white, null)

    init {
        val mainTypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PaintWithPaletteView,
            0,
            R.style.AppTheme
        )
        val firstColor =
            mainTypedArray.getColor(R.styleable.PaintWithPaletteView_firstColor, Color.RED)


        val secondColor =
            mainTypedArray.getColor(R.styleable.PaintWithPaletteView_secondColor, Color.GREEN)


        val thirdColor =
            mainTypedArray.getColor(R.styleable.PaintWithPaletteView_thirdColor, Color.BLUE)


        val fourthColor =
            mainTypedArray.getColor(R.styleable.PaintWithPaletteView_fourthColor, Color.BLACK)


        val fifthColor =
            mainTypedArray.getColor(R.styleable.PaintWithPaletteView_fifthColor, Color.WHITE)

        inflate(context, R.layout.view_paint_with_palette, this)
        paintView = findViewById(R.id.paintView)

        firstColorImageView = findViewById(R.id.first)
        firstColorImageView.initImageView(firstColor, paintView)

        secondColorImageView = findViewById(R.id.second)
        secondColorImageView.initImageView(secondColor, paintView)

        thirdColorImageView = findViewById(R.id.third)
        thirdColorImageView.initImageView(thirdColor, paintView)

        fourthColorImageView = findViewById(R.id.fourth)
        fourthColorImageView.initImageView(fourthColor, paintView)

        fifthColorImageView = findViewById(R.id.fifth)
        fifthColorImageView.initImageView(fifthColor, paintView)

        mainTypedArray.recycle()
    }
}

private fun ImageView.initImageView(color: Int, paintView: PaintView) {
    setColorFilter(
        color,
        PorterDuff.Mode.SRC_IN
    )
    setOnClickListener {
        paintView.setBrushColor(color)
    }
}
