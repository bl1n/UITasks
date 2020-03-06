package team.lf.uitasks.gmail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class AvatarBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<ImageView>(context, attributeSet) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        val appBarLayout = dependency as AppBarLayout

        val transformation = getTransformationFor(appBarLayout, child)
        transformImageView(transformation, child, appBarLayout)

        return super.onDependentViewChanged(parent, child, dependency)
    }

    private fun transformImageView(
        transformation: Float,
        child: ImageView,
        appBarLayout: AppBarLayout
    ) {
        val minHeight = appBarLayout.minimumHeight.toFloat()
        child.scaleY = transformation
        child.scaleX = maxOf(transformation, minHeight)
    }

    private fun getTransformationFor(appBarLayout: AppBarLayout, child: ImageView): Float {
        return (appBarLayout.totalScrollRange.toFloat() + appBarLayout.top.toFloat()) / appBarLayout.totalScrollRange.toFloat()
    }


}