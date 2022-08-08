package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

interface TodoItemsRepository {
    fun getTodoItems(): List<TodoItem>
    fun addTodoItem(todoItem: TodoItem)
}