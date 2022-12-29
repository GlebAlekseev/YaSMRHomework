package com.glebalekseevjk.yasmrhomework.ui.viewmodel.state

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel

data class TodoState(
    val isAuth: Boolean = false,
    val screenMode: String = TodoViewModel.MODE_ADD,
    val todoItem: TodoItem = TodoItem.PLUG,
    val isLoadingTodoItem: Boolean = true,
    val isDarkMode: Boolean = false,
)