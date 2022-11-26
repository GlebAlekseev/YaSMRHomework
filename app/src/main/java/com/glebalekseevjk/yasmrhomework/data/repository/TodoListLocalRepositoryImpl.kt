package com.glebalekseevjk.yasmrhomework.data.repository

import androidx.lifecycle.asFlow
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoListLocalRepositoryImpl(
    private val todoItemDao: TodoItemDao,
    private val mapper: Mapper<TodoItem, TodoItemDbModel>,
    private val revisionStorage: SharedPreferencesRevisionStorage,
) : TodoListLocalRepository {
    override fun getTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
        try {
            val revision = revisionStorage.getRevision() ?: Revision()
            todoItemDao.getAll().asFlow().collect {
                val list = it.map { mapper.mapDbModelToItem(it) }
                emit(Result(ResultStatus.SUCCESS, Pair(list, revision)))
            }
        } catch (err: Exception) {
            emit(Result(ResultStatus.FAILURE, Pair(emptyList(), Revision()), "Неизвестная ошибка"))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteTodoList(): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        flow<Result<Pair<List<TodoItem>, Revision>>> {
            emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
            try {
                val revision = revisionStorage.getRevision() ?: Revision()
                todoItemDao.deleteAll()
                emit(Result(ResultStatus.SUCCESS, Pair(emptyList(), revision)))
            } catch (err: Exception) {
                emit(
                    Result(
                        ResultStatus.FAILURE,
                        Pair(emptyList(), Revision()),
                        "Неизвестная ошибка"
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    override fun replaceTodoList(todoList: List<TodoItem>): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        flow {
            emit(Result(ResultStatus.LOADING, Pair(emptyList(), Revision())))
            try {
                val revision = revisionStorage.getRevision() ?: Revision()
                val todoListDb = todoList.map { mapper.mapItemToDbModel(it) }
                todoItemDao.replaceAll(todoListDb)
                emit(Result(ResultStatus.SUCCESS, Pair(todoList, revision)))
            } catch (err: Exception) {
                emit(
                    Result(
                        ResultStatus.FAILURE,
                        Pair(emptyList(), Revision()),
                        "Неизвестная ошибка"
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    override fun getTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(listOf(TodoItem.PLUG), Revision())))
        try {
            val revision = revisionStorage.getRevision() ?: Revision()
            val todoItemDb = todoItemDao.get(todoId)
            if (todoItemDb == null) {
                emit(
                    Result(
                        ResultStatus.FAILURE,
                        Pair(listOf(TodoItem.PLUG), revision),
                        "Задачи с указанным id не существует"
                    )
                )
            } else {
                val todoItem = mapper.mapDbModelToItem(todoItemDb)
                emit(Result(ResultStatus.SUCCESS, Pair(listOf(todoItem), revision)))
            }
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    Pair(listOf(TodoItem.PLUG), Revision()),
                    "Неизвестная ошибка"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun addTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        flow {
            emit(Result(ResultStatus.LOADING, Pair(listOf(TodoItem.PLUG), Revision())))
            try {
                val revision = revisionStorage.getRevision() ?: Revision()
                val todoItemDb = mapper.mapItemToDbModel(todoItem)
                todoItemDao.insert(todoItemDb)
                emit(Result(ResultStatus.SUCCESS, Pair(listOf(todoItem), revision)))
            } catch (err: Exception) {
                emit(
                    Result(
                        ResultStatus.FAILURE,
                        Pair(listOf(TodoItem.PLUG), Revision()),
                        "Неизвестная ошибка"
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteTodoItem(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> = flow {
        emit(Result(ResultStatus.LOADING, Pair(listOf(TodoItem.PLUG), Revision())))
        try {
            val revision = revisionStorage.getRevision() ?: Revision()
            val todoItemDb = todoItemDao.get(todoId)
            if (todoItemDb == null) {
                emit(
                    Result(
                        ResultStatus.FAILURE,
                        Pair(listOf(TodoItem.PLUG), revision),
                        "Задачи с указанным id не существует"
                    )
                )
            } else {
                val todoItem = mapper.mapDbModelToItem(todoItemDb)
                todoItemDao.delete(todoId)
                emit(Result(ResultStatus.SUCCESS, Pair(listOf(todoItem), revision)))
            }
        } catch (err: Exception) {
            emit(
                Result(
                    ResultStatus.FAILURE,
                    Pair(listOf(TodoItem.PLUG), Revision()),
                    "Неизвестная ошибка"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun editTodoItem(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        addTodoItem(todoItem)
}