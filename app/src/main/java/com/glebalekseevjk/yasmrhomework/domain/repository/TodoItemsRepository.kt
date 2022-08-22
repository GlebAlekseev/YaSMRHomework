package com.glebalekseevjk.yasmrhomework.domain.repository

import androidx.lifecycle.LiveData
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

interface TodoItemsRepository {
    fun getTodoList(): LiveData<List<TodoItem>>
    fun getTodoItem(id: String): TodoItem?
    fun addTodoItem(todoItem: TodoItem)
    fun deleteTodoItem(todoItem: TodoItem)
    fun deleteTodoItem(todoId: String)
    fun editTodoItem(todoItem: TodoItem)
}