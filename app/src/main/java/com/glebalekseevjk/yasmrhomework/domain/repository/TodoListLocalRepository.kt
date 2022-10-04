package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow
import com.glebalekseevjk.yasmrhomework.domain.entity.Result

interface TodoListLocalRepository {
    fun getTodoList(): Flow<Result<List<TodoItem>>>
    fun getTodoItem(id: String): Flow<Result<TodoItem>>
    fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>>
    fun deleteTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>>
    fun deleteTodoItem(todoId: String): Flow<Result<TodoItem>>
    fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>>
}