package com.glebalekseevjk.yasmrhomework.domain.repository

import androidx.lifecycle.LiveData
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getTodoList(): Flow<List<TodoItem>>
    fun getTodoItem(id: String): TodoItem?
    fun addTodoItem(todoItem: TodoItem)
    fun deleteTodoItem(todoItem: TodoItem)
    fun deleteTodoItem(todoId: String)
    fun editTodoItem(todoItem: TodoItem)
}