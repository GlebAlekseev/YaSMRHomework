package com.glebalekseevjk.yasmrhomework.data.repository

import androidx.lifecycle.asFlow
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.remote.TodoService
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronize.SynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class TodoListRepositoryImpl(
    private val todoItemDao: TodoItemDao,
    private val mapper: Mapper<TodoItem, TodoItemDbModel>,
    private val todoService: TodoService,
    private val revisionStorage: RevisionStorage,
    private val synchronizedStorage: SynchronizedStorage
) : TodoListRepository {
    override fun getTodoList(): Flow<Result<List<TodoItem>>> = flow {
        emit(Result(ResultStatus.LOADING, emptyList()))
        todoItemDao.getAll().asFlow().collect {
            val list = it.map { mapper.mapDbModelToItem(it) }
            emit(Result(ResultStatus.SUCCESS, list))
        }
    }.flowOn(Dispatchers.IO)

    override fun synchronizeTodoList(): Flow<Result<List<TodoItem>>> = flow{
        emit(Result(ResultStatus.LOADING, emptyList()))
        val todoListResponse = runCatching {
            todoService.getTodoList().execute()
        }.getOrNull()

        when (todoListResponse?.code()) {
            200 -> {
                val body = todoListResponse.body()
                todoItemDao.deleteAll()
                (body?.list ?: emptyList()).forEach {
                    todoItemDao.insert(mapper.mapItemToDbModel(it))
                }
                body?.let {
                    revisionStorage.setRevision(Revision(body.revision))
                }
                emit(Result(ResultStatus.SUCCESS, body?.list ?: emptyList()))
            }
            401 -> {
                emit(Result(ResultStatus.UNAUTHORIZED, emptyList()))
            }
            else -> {
                emit(Result(ResultStatus.FAILURE, emptyList()))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getTodoItem(todoId: Long): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        val todoItemDb = todoItemDao.get(todoId)
        if (todoItemDb == null) {
            emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
        } else {
            val todoItem = mapper.mapDbModelToItem(todoItemDb)
            emit(Result(ResultStatus.SUCCESS, todoItem))
        }
    }.flowOn(Dispatchers.IO)

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        // Добавить в Room
        todoItemDao.insert(mapper.mapItemToDbModel(todoItem))
        // Отправить на сервер
        val todoListResponse = runCatching {
            todoService.addTodoItem(todoItem).execute()
        }.getOrNull()
        when (todoListResponse?.code()) {
            200 -> {
                val body = todoListResponse.body()
                revisionStorage.setRevision(Revision(body!!.revision))
                emit(Result(ResultStatus.SUCCESS, body.list[0]))
            }
            401 -> {
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            }
            else -> {
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteTodoItem(todoId: Long): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        todoItemDao.delete(todoId)
        // Отправить на сервер
        val todoListResponse = runCatching {
            todoService.deleteTodoItem(todoId).execute()
        }.getOrNull()

        when (todoListResponse?.code()) {
            200 -> {
                val body = todoListResponse.body()
                revisionStorage.setRevision(Revision(body!!.revision))
                emit(Result(ResultStatus.SUCCESS, body.list[0]))
            }
            401 -> {
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            }
            else -> {
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<TodoItem>> = flow {
        emit(Result(ResultStatus.LOADING, TodoItem.PLUG))
        todoItemDao.insert(mapper.mapItemToDbModel(todoItem))

        // Отправить на сервер
        val todoListResponse = runCatching {
            todoService.putTodoItem(todoItem.id, todoItem).execute()
        }.getOrNull()


        when (todoListResponse?.code()) {
            200 -> {
                val body = todoListResponse.body()
                revisionStorage.setRevision(Revision(body!!.revision))
                emit(Result(ResultStatus.SUCCESS, body.list[0]))
            }
            401 ->{
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.UNAUTHORIZED, TodoItem.PLUG))
            }
            else -> {
                synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.UNSYNCHRONIZED)
                emit(Result(ResultStatus.FAILURE, TodoItem.PLUG))
            }
        }
    }.flowOn(Dispatchers.IO)
}