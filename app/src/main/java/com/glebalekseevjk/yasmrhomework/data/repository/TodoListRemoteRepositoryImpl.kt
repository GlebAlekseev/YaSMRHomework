package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.remote.TodoService
import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoListResponse
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class TodoListRemoteRepositoryImpl(
    private val todoService: TodoService
) : TodoListRepository {
    override fun getTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        val todoListResponse = runCatching {
            todoService.getTodoList().execute()
        }.getOrNull()
        val result = getResultFromTodoListResponse(todoListResponse)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun deleteTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        val todoListResponse = runCatching {
            todoService.deleteTodoList().execute()
        }.getOrNull()
        val result = getResultFromTodoListResponse(todoListResponse)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun replaceTodoList(todoList: List<TodoItem>): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        val todoListResponse = runCatching {
            todoService.patchTodoList(todoList).execute()
        }.getOrNull()
        val result = getResultFromTodoListResponse(todoListResponse)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun getTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        val todoListResponse = runCatching {
            todoService.getTodoItem(todoId).execute()
        }.getOrNull()
        val result = getResultFromTodoListResponse(todoListResponse)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        flow {
            emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
            val todoListResponse = runCatching {
                todoService.addTodoItem(todoItem).execute()
            }.getOrNull()
            val result = getResultFromTodoListResponse(todoListResponse)
            emit(result)
        }.flowOn(Dispatchers.IO)

    override fun deleteTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        val todoListResponse = runCatching {
            todoService.deleteTodoItem(todoId).execute()
        }.getOrNull()
        val result = getResultFromTodoListResponse(todoListResponse)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        flow {
            emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
            val todoListResponse = runCatching {
                todoService.putTodoItem(todoItem.id, todoItem).execute()
            }.getOrNull()
            val result = getResultFromTodoListResponse(todoListResponse)
            emit(result)
        }.flowOn(Dispatchers.IO)

    private fun getResultFromTodoListResponse(todoListResponse: Response<TodoListResponse>?): Result<Pair<List<TodoItem>, Revision>> {
        var message = ""
        var status = ResultStatus.SUCCESS
        var data = Pair(emptyList<TodoItem>(), Revision())
        todoListResponse?.body()?.revision ?: run {
            message = "Ошибка парсинга ответа"
        }
        todoListResponse ?: run { message = "Нет соединения" }
        todoListResponse?.code().let {
            when (it) {
                200 -> {
                    if (message == "") {
                        data = Pair(
                            todoListResponse!!.body()?.list ?: emptyList(),
                            Revision(todoListResponse.body()!!.revision)
                        )
                    }
                }
                400 -> {
                    message = "Ошибка клиента"
                }
                401 -> {
                    message = "Не авторизован"
                    status = ResultStatus.UNAUTHORIZED
                }
                500 -> {
                    message = "Ошибка сервера"
                }
                else -> {
                    message = "Неизвестная ошибка"
                }
            }
        }
        if (message != "") {
            status = ResultStatus.FAILURE
        }
        return Result(status, data, message)
    }
}