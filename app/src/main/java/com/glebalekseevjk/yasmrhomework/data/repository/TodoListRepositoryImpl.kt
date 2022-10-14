package com.glebalekseevjk.yasmrhomework.data.repository

import androidx.lifecycle.asFlow
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.remote.AuthService
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.remote.TodoService
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronized.SynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.awaitResponse

class TodoListRepositoryImpl(
    private val todoItemDao: TodoItemDao,
    private val mapper: Mapper<TodoItem, TodoItemDbModel>,
    private val todoService: TodoService,

): TodoListRepository {
    override fun getTodoList(): Flow<Result<List<TodoItem>>> = flow {
        emit(Result(ResultStatus.LOADING, emptyList()))

        todoItemDao.getAll().asFlow().collect{
            val list = it.map { mapper.mapDbModelToItem(it) }
            emit(Result(ResultStatus.SUCCESS, list))
        }

        val todoListResponse = todoService.getTodoList().awaitResponse()
        val list =  todoListResponse.body()?.list
        if (todoListResponse.code() == 200 && list != null){
            list.forEach{
                todoItemDao.insert(mapper.mapItemToDbModel(it))
            }
        }
        if (todoListResponse.code() == 401){
            emit(Result(ResultStatus.UNAUTHORIZED, emptyList()))
        }

    }.flowOn(Dispatchers.IO)

    override fun getTodoItem(todoId: Long): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        val todoItemDb = todoItemDao.get(todoId)
        if (todoItemDb == null){
            emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
        }else{
            val todoItem = mapper.mapDbModelToItem(todoItemDb)
            emit(Result(ResultStatus.SUCCESS, todoItem))
        }
    }.flowOn(Dispatchers.IO)

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        // Добавить в Room
        todoItemDao.insert(mapper.mapItemToDbModel(todoItem))

        // Отправить на сервер
        val todoListResponse = todoService.addTodoItem(todoItem).awaitResponse()
        when(todoListResponse.code()){
            200->emit(Result(ResultStatus.SUCCESS, todoListResponse.body()!!.list[0]))
            401->emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            else->emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteTodoItem(todoId: Long): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        todoItemDao.delete(todoId)
        // Отправить на сервер
        val todoListResponse = todoService.deleteTodoItem(todoId).awaitResponse()
        when(todoListResponse.code()){
            200->emit(Result(ResultStatus.SUCCESS, todoListResponse.body()!!.list[0]))
            401->emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            else->emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
        }
    }.flowOn(Dispatchers.IO)

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        todoItemDao.insert(mapper.mapItemToDbModel(todoItem))

        // Отправить на сервер
        val todoListResponse = todoService.putTodoItem(todoItem.id,todoItem).awaitResponse()
        when(todoListResponse.code()){
            200->emit(Result(ResultStatus.SUCCESS, todoListResponse.body()!!.list[0]))
            401->emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            else->emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
        }
    }.flowOn(Dispatchers.IO)
}