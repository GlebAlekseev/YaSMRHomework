package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.first


class TodoViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
    todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl
) : BaseViewModel(application) {
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage =
        SharedPreferencesTokenStorage(application)
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage =
        SharedPreferencesRevisionStorage(application)

    private val addTodoItemLocalUseCase = AddTodoItemUseCase(todoListLocalRepositoryImpl)
    private val editTodoItemLocalUseCase = EditTodoItemUseCase(todoListLocalRepositoryImpl)
    private val deleteTodoItemLocalUseCase = DeleteTodoItemUseCase(todoListLocalRepositoryImpl)
    private val getTodoItemLocalUseCase = GetTodoItemUseCase(todoListLocalRepositoryImpl)
    private val replaceTodoListLocalUseCase = ReplaceTodoListUseCase(todoListLocalRepositoryImpl)

    private val addTodoItemRemoteUseCase = AddTodoItemUseCase(todoListRemoteRepositoryImpl)
    private val editTodoItemRemoteUseCase = EditTodoItemUseCase(todoListRemoteRepositoryImpl)
    private val deleteTodoItemRemoteUseCase = DeleteTodoItemUseCase(todoListRemoteRepositoryImpl)
    private val logoutUseCase = LogoutUseCase(authRepositoryImpl)

    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    fun setCurrentTodoItemById(todoId: Long) {
        runBlockingWithExceptionHandler {
            val getResult =
                getTodoItemLocalUseCase(todoId).first { it.status != ResultStatus.LOADING }
            if (getResult.status == ResultStatus.SUCCESS) {
                currentTodoItem = getResult.data.first[0]
            } else {
                throw RuntimeException("БД не может получить элемент с id: $todoId")
            }
        }
    }

    fun addTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val addResult =
                addTodoItemLocalUseCase(todoItem).first { it.status != ResultStatus.LOADING }
            if (addResult.status == ResultStatus.SUCCESS) {
                addTodoItemRemoteUseCase(todoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        sharedPreferencesRevisionStorage.setRevision(response.second)
                        block(Result(it.status, it.data.first[0], it.message))
                    }else{
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может добавить todoItem: $todoItem")
            }
        }
    }

    fun editTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val editResult =
                editTodoItemLocalUseCase(todoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                editTodoItemRemoteUseCase(todoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        sharedPreferencesRevisionStorage.setRevision(response.second)
                        block(Result(it.status, it.data.first[0], it.message))
                    }else{
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может отредактировать todoItem: $todoItem")
            }
        }
    }


    fun deleteTodo(
        todoItem: TodoItem,
        snackBarBlock: (todoItem: TodoItem) -> Boolean,
        block: (Result<TodoItem>) -> Unit
    ) {
        viewModelScope.launchWithExceptionHandler {
            val deleteResult =
                deleteTodoItemLocalUseCase(todoItem.id).first { it.status != ResultStatus.LOADING }
            val deletedItem = if (deleteResult.status == ResultStatus.SUCCESS) {
                deleteResult.data.first[0]
            } else {
                throw RuntimeException("БД не может удалить запись todoItem: $todoItem")
            }
            if (snackBarBlock(deletedItem)) {
                val addResult =
                    addTodoItemLocalUseCase(deletedItem).first { it.status != ResultStatus.LOADING }
                if (addResult.status != ResultStatus.SUCCESS) throw RuntimeException("БД не может добавить todoItem: $deletedItem")
            } else {
                deleteTodoItemRemoteUseCase(deletedItem.id).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        sharedPreferencesRevisionStorage.setRevision(response.second)
                        block(Result(it.status, it.data.first[0], it.message))
                    }else{
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
            }
        }
    }

    val isAuth: Boolean
        get() {
            val tokenPair = sharedPreferencesTokenStorage.getTokenPair()
            return if (tokenPair == null) {
                logout()
                false
            } else true
        }

    private fun logout() {
        viewModelScope.launchWithExceptionHandler {
            logoutUseCase()
        }
    }

    var currentTodoItem: TodoItem = TodoItem.PLUG
}
