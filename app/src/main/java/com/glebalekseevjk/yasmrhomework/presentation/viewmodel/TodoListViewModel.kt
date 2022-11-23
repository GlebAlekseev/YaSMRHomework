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
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class TodoListViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
    todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl
) : BaseViewModel(application) {
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage =
        SharedPreferencesTokenStorage(application)
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage =
        SharedPreferencesRevisionStorage(application)

    private val editTodoItemLocalUseCase = EditTodoItemUseCase(todoListLocalRepositoryImpl)
    private val deleteTodoItemLocalUseCase = DeleteTodoItemUseCase(todoListLocalRepositoryImpl)
    private val getTodoListLocalUseCase = GetTodoListUseCase(todoListLocalRepositoryImpl)
    private val deleteTodoListLocalUseCase = DeleteTodoListUseCase(todoListLocalRepositoryImpl)
    private val addTodoItemLocalUseCase = AddTodoItemUseCase(todoListLocalRepositoryImpl)
    private val replaceTodoListLocalUseCase = ReplaceTodoListUseCase(todoListLocalRepositoryImpl)

    private val editTodoItemRemoteUseCase = EditTodoItemUseCase(todoListRemoteRepositoryImpl)
    private val deleteTodoItemRemoteUseCase = DeleteTodoItemUseCase(todoListRemoteRepositoryImpl)
    private val getTodoListRemoteUseCase = GetTodoListUseCase(todoListRemoteRepositoryImpl)

    private val logoutUseCase = LogoutUseCase(authRepositoryImpl)

    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
            when (coroutineContext) {
                getTodoListJob -> {
                    _todoListViewState.value = _todoListViewState.value.copy()
                }
            }
        }

    private var getTodoListJob: Job? = null

    private var _todoListViewState = MutableStateFlow(TodoListViewState.PLUG)
    val todoListViewState: StateFlow<TodoListViewState> by lazy {
        observeTodoList()
        _todoListViewState
    }

    private fun observeTodoList() {
        getTodoListJob = viewModelScope.launchWithExceptionHandler {
            getTodoListLocalUseCase().collect {
                _todoListViewState.value = _todoListViewState.value.copy(
                    result = Result(
                        it.status,
                        it.data.first,
                        it.message
                    )
                )
            }
        }
    }

    fun synchronizeTodoList(block: (Result<List<TodoItem>>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            getTodoListRemoteUseCase().collect {
                if (it.status == ResultStatus.SUCCESS) {
                    val response = it.data
                    val replaceResult =
                        replaceTodoListLocalUseCase(response.first).first { it.status != ResultStatus.LOADING }
                    if (replaceResult.status == ResultStatus.SUCCESS) {
                        sharedPreferencesRevisionStorage.setRevision(response.second)
                    } else {
                        throw RuntimeException("БД не может заменить все записи")
                    }
                }
                block(Result(it.status, it.data.first, it.message))
            }
        }
    }

    fun deleteTodo(
        todoItem: TodoItem,
        snackBarBlock: suspend (todoItem: TodoItem) -> Boolean,
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
                    } else {
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
            }
        }
    }

    fun finishTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        val newTodoItem = todoItem.copy(done = true)
        viewModelScope.launchWithExceptionHandler {
            val editResult =
                editTodoItemLocalUseCase(newTodoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                editTodoItemRemoteUseCase(newTodoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        sharedPreferencesRevisionStorage.setRevision(response.second)
                        block(Result(it.status, it.data.first[0], it.message))
                    } else {
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может отредактировать todoItem: $newTodoItem")
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

    var isViewFinished = MutableStateFlow(true)
}
