package com.glebalekseevjk.yasmrhomework.ui.behavior

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import kotlin.math.min


class FloatingActionButtonBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<FloatingActionButton>() {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        val translationY =
            min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ) {
        ObjectAnimator.ofFloat(child, "translationY", 0f).apply {
            duration = 200
            interpolator = AccelerateInterpolator()
            start()
        }
        super.onDependentViewRemoved(parent, child, dependency)
    }
}