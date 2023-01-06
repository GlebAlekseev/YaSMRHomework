package com.glebalekseevjk.yasmrhomework.ui.rv

import android.view.View
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

abstract class SwipeControllerActions {
    open fun onLeftClicked(view: View, todoItem: TodoItem) {}
    open fun onRightClicked(view: View, todoItem: TodoItem) {}
}