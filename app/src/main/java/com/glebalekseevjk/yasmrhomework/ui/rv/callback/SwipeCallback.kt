package com.glebalekseevjk.yasmrhomework.ui.rv.callback

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.rv.adapter.TaskListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.*
import kotlin.math.abs

class SwipeCallback constructor(private val scrollConstraintOffset: Float) : Callback() {
    private var isFirst = false
    private var startScrollX = 0
    private var boundaryScrollX = 0
    private var boundaryOffset = 0f

    // LinkedList, чтобы избежать ConcurrentModificationException
    private val viewHolderItemIdList: LinkedList<View> = LinkedList()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            0,
            LEFT or RIGHT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    // Положение, с которого произойдет пролистывание без учета скорости
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Float.MAX_VALUE
    }

    // Скорость, начиная с которой происходит пролистывание
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
    }

    // Отрисовка itemView
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (dX == 0f) {
                isFirst = true
                startScrollX = viewHolder.itemView.scrollX
                // Добавить если нету
                if (!viewHolderItemIdList.contains(viewHolder.itemView)) {
                    viewHolderItemIdList.add(viewHolder.itemView)
                }
                // Всех кроме меня закрыть и убрать из списка
                viewHolderItemIdList.forEach { view ->
                    if (view != viewHolder.itemView) {
                        CoroutineScope(Dispatchers.Main).launch {
                            ValueAnimator.ofFloat(boundaryScrollX.toFloat(), 0f).apply {
                                duration = 200
                                addUpdateListener {
                                    view.scrollTo((it.animatedValue as Float).toInt(),0)
                                }
                                start()
                            }
                        }
                        viewHolderItemIdList.remove(view)
                    }
                }
            }

            if (isCurrentlyActive) {
                val scrollOffset = getScrollOffsetWithConstraint(startScrollX, dX)
                viewHolder.itemView.scrollTo(scrollOffset, 0)
            } else {
                if (isFirst) {
                    isFirst = false
                    boundaryScrollX = viewHolder.itemView.scrollX
                    boundaryOffset = dX
                }

                val scroll = abs(viewHolder.itemView.scrollX)
                if (scroll in 1 until scrollConstraintOffset.toInt()) {
                    viewHolder.itemView.scrollTo(
                        (boundaryScrollX * dX / boundaryOffset).toInt(),
                        0
                    )
                }
            }
        }
    }

    private fun getScrollOffsetWithConstraint(startScrollX: Int, dX: Float): Int {
        val scrollWithOffset = startScrollX - dX
        return MathUtils.clamp(scrollWithOffset, -scrollConstraintOffset, scrollConstraintOffset)
            .toInt()
    }

    fun resetViewHolder(view: View) {
        if (viewHolderItemIdList.contains(view)){
            view.scrollTo(0, 0)
            viewHolderItemIdList.remove(view)
        }
    }
}