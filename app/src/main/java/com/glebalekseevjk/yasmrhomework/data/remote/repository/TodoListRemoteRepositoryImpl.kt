package com.glebalekseevjk.yasmrhomework.data.remote.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListLocalRepository
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRemoteRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException
import java.time.LocalDateTime

class TodoListRemoteRepositoryImpl: TodoListRemoteRepository {
    override fun getTodoList(): Flow<Result<List<TodoItem>>> {
        TODO("Not yet implemented")
    }

    override fun getTodoItem(id: String): Flow<Result<TodoItem>> {
        TODO("Not yet implemented")
    }

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> {
        TODO("Not yet implemented")
    }

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> {
        TODO("Not yet implemented")
    }

    override fun deleteTodoItem(todoId: String): Flow<Result<TodoItem>> {
        TODO("Not yet implemented")
    }

}