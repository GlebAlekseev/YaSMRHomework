package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow
import com.glebalekseevjk.yasmrhomework.domain.entity.Result

interface TodoListRepository {
    fun getTodoList(): Flow<Result<List<TodoItem>>>
    fun getTodoItem(todoId: Long): Flow<Result<TodoItem>>
    fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>>
    fun deleteTodoItem(todoId: Long): Flow<Result<TodoItem>>
    fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>>
}