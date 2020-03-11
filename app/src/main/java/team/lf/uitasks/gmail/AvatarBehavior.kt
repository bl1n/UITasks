package team.lf.uitasks.gmail

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginStart
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.max

class AvatarBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<ImageView>(context, attributeSet) {

    private var startX: Float = 0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ImageView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        startX = child.x
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        val appBarLayout = dependency as AppBarLayout

        val transformation = getTransformationFor(appBarLayout, child)
        transformImageView(transformation, child, appBarLayout)
        val allRoute = child.x
        moveImageView(transformation, child, appBarLayout, allRoute)

        return super.onDependentViewChanged(parent, child, dependency)
    }

    private fun moveImageView(
        transformation: Float,
        child: ImageView,
        appBarLayout: AppBarLayout,
        allRoute: Float
    ) {
        Log.d("TAG", "$startX")
        if (transformation <= 0.5f) {
            child.x -=1

        }
    }

    private fun transformImageView(
        transformation: Float,
        child: ImageView,
        appBarLayout: AppBarLayout
    ) {
        child.scaleY = max(transformation, MIN_SCALE)
        child.scaleX = max(transformation, MIN_SCALE)
    }

    private fun getTransformationFor(appBarLayout: AppBarLayout, child: ImageView): Float {
        return (appBarLayout.totalScrollRange.toFloat() + appBarLayout.top.toFloat()) / appBarLayout.totalScrollRange.toFloat()
    }

    companion object {
        val MIN_SCALE = 0.5f
    }

}