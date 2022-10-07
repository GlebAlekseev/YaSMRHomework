package com.glebalekseevjk.yasmrhomework.data.remote.model

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

data class TodoItemResponse(
    val status: Int,
    val item: TodoItem?,
    val revision: Long,
    val message: String,
)