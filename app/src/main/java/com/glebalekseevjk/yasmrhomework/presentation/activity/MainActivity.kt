package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R


class MainActivity : AppCompatActivity() {
    data class OffsetY(var y: Int = 0,var time: Long=0L)
    operator fun OffsetY.minus(b: OffsetY) = OffsetY(y-b.y,time-b.time)
    private var lastOffset: OffsetY = OffsetY()


    private val linearLayout: LinearLayout by lazy { findViewById(R.id.linearLayout) }
    private val countDoneTextView: TextView by lazy {  findViewById(R.id.count_done) }
    private val recyclerView: RecyclerView by lazy {  findViewById(R.id.rv_task_list) }
    private val dp: Float by lazy{ resources.displayMetrics.density }

    private var stabilizedAnimation = false
    private var heightCountDoneTextView = 0
    private var paddingLeftLinearLayout = 0
    private var paddingRightLinearLayout = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            val currentOffset = OffsetY(ev.y.toInt()/2,ev.eventTime)
            if (ev.action == MotionEvent.ACTION_DOWN){
                lastOffset = currentOffset
                return@let
            }

            val maxPaddingTop = (90*dp).toInt()+1
            val minPaddingTop = (15*dp).toInt()
            val offset = currentOffset - lastOffset
            var newPadding: Int = linearLayout.paddingTop + offset.y*dp.toInt()

            if (newPadding >= maxPaddingTop && linearLayout.paddingTop == maxPaddingTop ||
                newPadding <= minPaddingTop && linearLayout.paddingTop == minPaddingTop ) return@let

            if (newPadding > maxPaddingTop ) newPadding = maxPaddingTop
            if (newPadding < minPaddingTop) newPadding = minPaddingTop

            linearLayout.updatePadding(top = newPadding)
            lastOffset = currentOffset

            if (stabilizedAnimation){
                val valueHeightAnimator = ValueAnimator.ofInt(countDoneTextView.height,heightCountDoneTextView)
                valueHeightAnimator.duration = 100
                valueHeightAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    countDoneTextView.updateLayoutParams { height = animatedValue }
                }
                valueHeightAnimator.start()
                val valuePaddingLeftAnimator = ValueAnimator.ofInt(linearLayout.paddingLeft,paddingLeftLinearLayout)
                valuePaddingLeftAnimator.duration = 100
                valuePaddingLeftAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    linearLayout.updatePadding(left = animatedValue)
                }
                valuePaddingLeftAnimator.start()
                val valuePaddingRightAnimator = ValueAnimator.ofInt(linearLayout.paddingRight,paddingRightLinearLayout)
                valuePaddingRightAnimator.duration = 100
                valuePaddingRightAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    linearLayout.updatePadding(right = animatedValue)
                }
                valuePaddingRightAnimator.start()
                stabilizedAnimation = false
                recyclerView.setBackgroundResource(R.drawable.round_corner)
                linearLayout.setBackgroundResource(R.drawable.null_header)
            }

            if (newPadding == minPaddingTop){
                heightCountDoneTextView = countDoneTextView.height
                paddingLeftLinearLayout = linearLayout.paddingLeft
                paddingRightLinearLayout = linearLayout.paddingRight
                val valueHeightAnimator = ValueAnimator.ofInt(countDoneTextView.height,1)
                    valueHeightAnimator.duration = 100
                    valueHeightAnimator.addUpdateListener {
                        val animatedValue = it.animatedValue as Int
                        countDoneTextView.updateLayoutParams { height = animatedValue }
                    }
                    valueHeightAnimator.start()
                val valuePaddingLeftAnimator = ValueAnimator.ofInt(linearLayout.paddingLeft,(15*dp).toInt())
                    valuePaddingLeftAnimator.duration = 100
                    valuePaddingLeftAnimator.addUpdateListener {
                        val animatedValue = it.animatedValue as Int
                        linearLayout.updatePadding(left = animatedValue)
                    }
                    valuePaddingLeftAnimator.start()
                val valuePaddingRightAnimator = ValueAnimator.ofInt(linearLayout.paddingRight,(25*dp).toInt())
                    valuePaddingRightAnimator.duration = 100
                    valuePaddingRightAnimator.addUpdateListener {
                        val animatedValue = it.animatedValue as Int
                        linearLayout.updatePadding(right = animatedValue)
                    }
                    valuePaddingRightAnimator.start()
                stabilizedAnimation = true

                recyclerView.setBackgroundResource(R.drawable.bottom_round_corner)
                linearLayout.setBackgroundResource(R.drawable.drop_down_shadow)
            }
        }
        return super.dispatchTouchEvent(ev)
    }



}