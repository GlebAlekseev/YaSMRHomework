package com.glebalekseevjk.yasmrhomework.domain.repository

import androidx.lifecycle.LiveData
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getTodoList(): Flow<List<TodoItem>>
    fun getTodoItem(id: String): TodoItem?
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItem(todoId: String)
    suspend fun editTodoItem(todoItem: TodoItem)
}