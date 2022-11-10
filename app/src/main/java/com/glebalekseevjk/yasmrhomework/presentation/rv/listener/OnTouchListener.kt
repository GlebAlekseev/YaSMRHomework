package com.glebalekseevjk.yasmrhomework.presentation.rv.listener

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R

class OnTouchListener(
    private val linearLayout: LinearLayout,
    private val countDoneTextView: TextView,
    private val dp: Float,
) : View.OnTouchListener {

    override fun onTouch(view: View?, ev: MotionEvent?): Boolean {
        val recyclerView = view as RecyclerView
        ev?.let {
            val currentOffset = it.y.toInt() / 2
            if (it.action == MotionEvent.ACTION_DOWN) {
                TouchEventSettings.lastOffsetY = currentOffset
                return@let
            }
            if (it.action == MotionEvent.ACTION_MOVE) return@let
            val firstItemPos =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (currentOffset > 0 && firstItemPos > 5) return@let

            val offset = currentOffset - TouchEventSettings.lastOffsetY
            var newPadding: Int = linearLayout.paddingTop + offset * dp.toInt()

            if (newPadding >= TouchEventSettings.maxPaddingTop && linearLayout.paddingTop == TouchEventSettings.maxPaddingTop ||
                newPadding <= TouchEventSettings.minPaddingTop && linearLayout.paddingTop == TouchEventSettings.minPaddingTop
            ) return@let

            if (newPadding > TouchEventSettings.maxPaddingTop) newPadding =
                TouchEventSettings.maxPaddingTop
            if (newPadding < TouchEventSettings.minPaddingTop) newPadding =
                TouchEventSettings.minPaddingTop
            linearLayout.updatePadding(top = newPadding)


            if (TouchEventSettings.stabilizedAnimation) {
                animateValue(
                    countDoneTextView.height,
                    TouchEventSettings.heightCountDoneTextView
                ) {
                    countDoneTextView.updateLayoutParams { height = it }
                }
                animateValue(
                    linearLayout.paddingLeft,
                    TouchEventSettings.paddingLeftLinearLayout
                ) {
                    linearLayout.updatePadding(left = it)
                }
                animateValue(
                    linearLayout.paddingRight,
                    TouchEventSettings.paddingRightLinearLayout
                ) {
                    linearLayout.updatePadding(right = it)
                }
                TouchEventSettings.stabilizedAnimation = false
                recyclerView.setBackgroundResource(R.drawable.round_corner)
                linearLayout.elevation = 0f
            }
            if (newPadding == TouchEventSettings.minPaddingTop) {
                TouchEventSettings.heightCountDoneTextView = countDoneTextView.height
                TouchEventSettings.paddingLeftLinearLayout = linearLayout.paddingLeft
                TouchEventSettings.paddingRightLinearLayout = linearLayout.paddingRight
                animateValue(countDoneTextView.height, 1) {
                    countDoneTextView.updateLayoutParams { height = it }
                }
                animateValue(linearLayout.paddingLeft, (15 * dp).toInt()) {
                    linearLayout.updatePadding(left = it)
                }
                animateValue(linearLayout.paddingRight, (25 * dp).toInt()) {
                    linearLayout.updatePadding(right = it)
                }
                TouchEventSettings.stabilizedAnimation = true
                recyclerView.setBackgroundResource(R.drawable.bottom_round_corner)
                linearLayout.elevation = 15f
            }
            TouchEventSettings.lastOffsetY = currentOffset
        }
        return false
    }

    companion object {
        private fun animateValue(
            start: Int,
            end: Int,
            duration: Long = 100,
            function: (animatedValue: Int) -> Unit,
        ) {
            val valueAnimator = ValueAnimator.ofInt(start, end)
            valueAnimator.duration = duration
            valueAnimator.addUpdateListener {
                val animatedValue = it.animatedValue as Int
                function.invoke(animatedValue)
            }
            valueAnimator.start()
        }

        object TouchEventSettings {
            var stabilizedAnimation: Boolean = false
            var heightCountDoneTextView: Int = 0
            var paddingLeftLinearLayout: Int = 0
            var paddingRightLinearLayout: Int = 0
            var maxPaddingTop = 0
            var minPaddingTop = 0
            var lastOffsetY: Int = 0
        }
    }
}