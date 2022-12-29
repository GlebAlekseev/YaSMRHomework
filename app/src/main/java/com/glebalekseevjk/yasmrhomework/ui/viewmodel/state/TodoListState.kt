package com.glebalekseevjk.yasmrhomework.ui.viewmodel.state

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

data class TodoListState(
    val isAuth: Boolean = false,
    val isDarkMode: Boolean = false,
    val isShowFinished: Boolean = false,
    val listTodoItem: List<TodoItem> = emptyList(),
    val isLoadingListTodoItem: Boolean = true
)