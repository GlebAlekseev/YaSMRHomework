package com.glebalekseevjk.yasmrhomework.ui.rv.callback

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R

enum class ButtonShowedState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

@SuppressLint("ClickableViewAccessibility")
class SwipeController constructor(val swipeControllerActions: SwipeControllerActions) : Callback() {
    private var swipeBack = false
    private var buttonWH = 0
    private var buttonShowedState = ButtonShowedState.GONE
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var buttonInstance: RectF? = null
    private var btnIconSize: Int = 0

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonShowedState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        _dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        with(viewHolder.itemView) {
            buttonWH = bottom - top
            btnIconSize = (28 * resources.displayMetrics.density).toInt()
        }
        var dX = _dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonShowedState.GONE) {
                if (Math.abs(dX) <= 2 * buttonWH) {
                    if (buttonShowedState == ButtonShowedState.LEFT_VISIBLE) dX =
                        Math.max(dX, buttonWH.toFloat());
                    if (buttonShowedState == ButtonShowedState.RIGHT_VISIBLE) dX =
                        Math.min(dX, -buttonWH.toFloat());
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        if (buttonShowedState == ButtonShowedState.GONE) {
            if (_dX > 2 * buttonWH) {
                dX = (2 * buttonWH).toFloat()

            } else if (_dX < -2 * buttonWH) {
                dX = -(2 * buttonWH).toFloat()
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
        with(viewHolder.itemView) {
            elevation = 0f

        }
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            event?.let {
                swipeBack =
                    it.action == MotionEvent.ACTION_CANCEL || it.action == MotionEvent.ACTION_UP
            }
            if (swipeBack) {
                if (dX < -buttonWH) buttonShowedState = ButtonShowedState.RIGHT_VISIBLE
                else if (dX > buttonWH) buttonShowedState = ButtonShowedState.LEFT_VISIBLE
                if (buttonShowedState != ButtonShowedState.GONE) {
                    recyclerView.isClickable = false
                    setTouchUpListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setScrollChangeListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
            false
        }
    }


    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _erg, event ->
            event?.let {
                if (event.action == MotionEvent.ACTION_UP) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        0f,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    recyclerView.isClickable = true
                    swipeBack = false
                    currentItemViewHolder = null
                    recyclerView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int -> }
                    recyclerView.setOnTouchListener { _, _ -> false }
                    if (buttonInstance != null && buttonInstance!!.contains(event.x, event.y)) {
                        if (buttonShowedState == ButtonShowedState.LEFT_VISIBLE) {
                            swipeControllerActions.onLeftClicked(viewHolder.bindingAdapterPosition)
                        } else if (buttonShowedState == ButtonShowedState.RIGHT_VISIBLE) {
                            swipeControllerActions.onRightClicked(viewHolder.bindingAdapterPosition)
                        }
                    }
                    buttonShowedState = ButtonShowedState.GONE
                }
            }
            false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setScrollChangeListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            recyclerView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int -> }
            recyclerView.setOnTouchListener { _, _ -> false }
            recyclerView.isClickable = true
            swipeBack = false
            buttonShowedState = ButtonShowedState.GONE
            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                0f,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val p = Paint()
        with(viewHolder.itemView) {
            val verticalPadding = (bottom - top - buttonWH) / 2

            val leftButton = RectF(
                left.toFloat(),
                top.toFloat() - verticalPadding,
                left.toFloat() + buttonWH,
                bottom.toFloat() + verticalPadding
            )
            p.color = Color.GREEN
            c.drawRect(leftButton, p)

            val rightButton = RectF(
                right - buttonWH.toFloat(),
                top.toFloat() - verticalPadding,
                right.toFloat(),
                bottom.toFloat() + verticalPadding
            )
            p.color = Color.RED
            c.drawRect(rightButton, p)

            val drawableRemove = context.getDrawable(R.drawable.ic_baseline_remove_24)
            val drawableAccept = context.getDrawable(R.drawable.ic_baseline_accept_24)
            drawableRemove?.setTint(Color.WHITE)
            drawableAccept?.setTint(Color.WHITE)

            val padding = (bottom - top - btnIconSize) / 2

            drawableRemove?.setBounds(
                right - buttonWH + padding,
                top + padding,
                right - buttonWH + padding + btnIconSize,
                top + padding + btnIconSize
            )
            drawableAccept?.setBounds(
                left + padding,
                top + padding,
                left + padding + btnIconSize,
                top + padding + btnIconSize
            )

            drawableRemove?.draw(c)
            drawableAccept?.draw(c)

            buttonInstance = null
            if (buttonShowedState == ButtonShowedState.RIGHT_VISIBLE) {
                buttonInstance = rightButton
            } else if (buttonShowedState == ButtonShowedState.LEFT_VISIBLE) {
                buttonInstance = leftButton
            }
        }
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {
            drawButtons(c, it)
        }
    }
}