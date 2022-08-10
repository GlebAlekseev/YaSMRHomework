package com.glebalekseevjk.yasmrhomework.presentation.rv.callback

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

enum class ButtonShowedState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}
@SuppressLint("ClickableViewAccessibility")
class SwipeController constructor(val swipeControllerActions: SwipeControllerActions): Callback() {
    private var swipeBack = false
    private val buttonWidth = 300
    private var buttonShowedState = ButtonShowedState.GONE
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var buttonInstance: RectF? = null


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
        if (swipeBack ){
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
        var dX = _dX
        if (actionState == ACTION_STATE_SWIPE){
            if (buttonShowedState != ButtonShowedState.GONE){
                if (buttonShowedState == ButtonShowedState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth.toFloat());
                if (buttonShowedState == ButtonShowedState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth.toFloat());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }else{
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        if (buttonShowedState == ButtonShowedState.GONE){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ){
         recyclerView.setOnTouchListener { _, event ->
             event?.let {
                 swipeBack =
                     it.action == MotionEvent.ACTION_CANCEL || it.action == MotionEvent.ACTION_UP
             }
             if (swipeBack) {
                 if (dX < -buttonWidth) buttonShowedState = ButtonShowedState.RIGHT_VISIBLE
                 else if (dX > buttonWidth) buttonShowedState = ButtonShowedState.LEFT_VISIBLE
                 if (buttonShowedState != ButtonShowedState.GONE) {
                     recyclerView.isClickable = false
                     setTouchUpListener(c,
                         recyclerView,
                         viewHolder,
                         dX,
                         dY,
                         actionState,
                         isCurrentlyActive)
                     setScrollChangeListener(c,
                         recyclerView,
                         viewHolder,
                         dX,
                         dY,
                         actionState,
                         isCurrentlyActive)
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
    ){
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
                    recyclerView.setOnScrollChangeListener{ view: View, i: Int, i1: Int, i2: Int, i3: Int -> }
                    recyclerView.setOnTouchListener { _, _ -> false }
                    if (swipeControllerActions != null && buttonInstance != null && buttonInstance!!.contains(event.x,event.y)){
                        if (buttonShowedState == ButtonShowedState.LEFT_VISIBLE){
                            swipeControllerActions.onLeftClicked(viewHolder.bindingAdapterPosition)
                        }else if(buttonShowedState == ButtonShowedState.RIGHT_VISIBLE){
                            swipeControllerActions.onRightClicked(viewHolder.bindingAdapterPosition)
                        }
                    }
                    buttonShowedState = ButtonShowedState.GONE
                }
            }
            false
        }
    }

    private fun setScrollChangeListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ){
        recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            recyclerView.setOnScrollChangeListener{ view: View, i: Int, i1: Int, i2: Int, i3: Int -> }
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


    private fun drawButtons(c: Canvas,viewHolder: RecyclerView.ViewHolder){

        val itemView = viewHolder.itemView
        val p = Paint()

        val leftButton = RectF(itemView.left.toFloat(),itemView.top.toFloat(),itemView.left.toFloat() + buttonWidth,itemView.bottom.toFloat())
        p.color = Color.GREEN
        c.drawRect(leftButton,p)

        val rightButton = RectF(itemView.right - buttonWidth.toFloat(),itemView.top.toFloat(),itemView.right.toFloat(),itemView.bottom.toFloat())
        p.color = Color.RED
        c.drawRect(rightButton,p)

        buttonInstance = null
        if (buttonShowedState == ButtonShowedState.RIGHT_VISIBLE){
            buttonInstance = rightButton
        }else if(buttonShowedState == ButtonShowedState.LEFT_VISIBLE){
            buttonInstance = leftButton
        }


    }

    fun onDraw(c: Canvas){
        currentItemViewHolder?.let {
            drawButtons(c, it)
        }
    }
}