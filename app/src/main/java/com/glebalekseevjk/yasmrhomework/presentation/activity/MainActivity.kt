package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.rv.adapter.TaskListAdapter
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeController
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeControllerActions
import java.text.FieldPosition

object TouchEventSettings {
    var stabilizedAnimation: Boolean = false
    var heightCountDoneTextView: Int = 0
    var paddingLeftLinearLayout: Int = 0
    var paddingRightLinearLayout: Int = 0
    var lastOffsetY: Int = 0
    var maxPaddingTop = 0
    var minPaddingTop = 0
}

class MainActivity : AppCompatActivity() {
    private val linearLayout: LinearLayout by lazy { findViewById(R.id.linearLayout) }
    private val countDoneTextView: TextView by lazy { findViewById(R.id.count_done) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.rv_task_list) }
    private val dp: Float by lazy { resources.displayMetrics.density }
    private val repositoryImpl by lazy { TodoItemsRepositoryImpl() }
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDispatchTouchEventSettings()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        taskListAdapter = TaskListAdapter()
        with(recyclerView){
            adapter = taskListAdapter
            taskListAdapter.taskList = repositoryImpl.getTodoItems()
            val swipeController = SwipeController(object: SwipeControllerActions() {
                override fun onLeftClicked(position: Int){
                    // Завершение
                    Log.d("MainActivity","finished on position $position")
                }

                override fun onRightClicked(position: Int) {
                    // Удаление
                    Log.d("MainActivity","removed on position $position")
                }
            })
            ItemTouchHelper(swipeController).attachToRecyclerView(this)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeController.onDraw(c)
                }
            })
        }
    }


    private fun initDispatchTouchEventSettings(){
        TouchEventSettings.maxPaddingTop = (90 * dp).toInt() + 1
        TouchEventSettings.minPaddingTop = (15 * dp).toInt()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            val currentOffset = it.y.toInt() / 2
            if (it.action == MotionEvent.ACTION_DOWN) {
                TouchEventSettings.lastOffsetY = currentOffset
                return@let
            }

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
                animateValue(countDoneTextView.height,
                    TouchEventSettings.heightCountDoneTextView) {
                    countDoneTextView.updateLayoutParams { height = it }
                }
                animateValue(linearLayout.paddingLeft,
                    TouchEventSettings.paddingLeftLinearLayout) {
                    linearLayout.updatePadding(left = it)
                }
                animateValue(linearLayout.paddingRight,
                    TouchEventSettings.paddingRightLinearLayout) {
                    linearLayout.updatePadding(right = it)
                }
                TouchEventSettings.stabilizedAnimation = false
                recyclerView.setBackgroundResource(R.drawable.round_corner)
                linearLayout.setBackgroundResource(R.drawable.null_header)
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
                linearLayout.setBackgroundResource(R.drawable.drop_down_shadow)
            }
            TouchEventSettings.lastOffsetY = currentOffset
        }
        return super.dispatchTouchEvent(ev)
    }


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

}