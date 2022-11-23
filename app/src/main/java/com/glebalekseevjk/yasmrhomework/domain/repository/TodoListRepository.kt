package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoListRepository {
    fun getTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>>
    fun deleteTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>>
    fun getTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>>
    fun addTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>>
    fun deleteTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>>
    fun editTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>>
}