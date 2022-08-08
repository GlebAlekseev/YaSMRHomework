package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.adapter.TaskListAdapter

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
        }
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                Log.d("%%%onMove", "target: $target layoutPosition: ${viewHolder.layoutPosition}")
                return false
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
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    println("onChildDraw dX: $dX")
                    var offsetX: Float = dX
                    if (dX <= -250) offsetX = -249f
                    if (dX >= 250) offsetX = 249f



                    if (offsetX > -250 && offsetX < 0){
                        viewHolder.itemView.translationX = offsetX
                        with(viewHolder.itemView){
                            c.drawRect(
                                right.toFloat(),
                                top.toFloat(),
                                right.toFloat()+offsetX,
                                bottom.toFloat(),
                                Paint().apply { color = ContextCompat.getColor(context,R.color.red) }
                            )
                        }

                    }
                    if (offsetX > 0 && offsetX < 250){
                        viewHolder.itemView.translationX = offsetX
                        with(viewHolder.itemView){
                            c.drawRect(
                                left.toFloat(),
                                top.toFloat(),
                                left.toFloat()+offsetX,
                                bottom.toFloat(),
                                Paint().apply { color = ContextCompat.getColor(context,R.color.green) }
                            )
                        }
                    }

                }else{
                    super.onChildDraw(c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive)
                }
            }

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                fromPos: Int,
                target: RecyclerView.ViewHolder,
                toPos: Int,
                x: Int,
                y: Int
            ) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                println("onMoved")
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                println("onSelectedChanged")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("%%%onSwiped", "direction: $direction layoutPosition: ${viewHolder.layoutPosition}")
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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