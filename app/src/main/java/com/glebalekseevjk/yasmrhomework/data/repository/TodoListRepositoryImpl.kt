package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.*

class TodoListRepositoryImpl: TodoListRepository {
    override fun getTodoList(): Flow<Result<List<TodoItem>>> {
        return flow {  }
    }

    override fun getTodoItem(todoId: Long): Flow<Result<TodoItem>> {
        return flow {  }
    }

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> {
        return flow {  }
    }

    override fun deleteTodoItem(todoId: Long): Flow<Result<TodoItem>> {
        return flow {  }
    }

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> {
        return flow {  }
    }


}