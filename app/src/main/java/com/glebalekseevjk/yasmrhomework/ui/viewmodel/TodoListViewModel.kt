package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class TodoListViewModel(
    authRepository: AuthRepository,
    tokenRepository: TokenRepository,
    revisionRepository: RevisionRepository,
    todoListLocalRepository: TodoListLocalRepository,
    todoListRemoteRepository: TodoListRemoteRepository,
    workManagerRepository: WorkManagerRepository
) : BaseViewModel() {
    private val authUseCase = AuthUseCase(authRepository)
    private val tokenUseCase = TokenUseCase(tokenRepository)
    private val revisionUseCase = RevisionUseCase(revisionRepository)
    private val todoItemUseCase = TodoItemUseCase(todoListLocalRepository, todoListRemoteRepository)
    private val workManagerUseCase = WorkManagerUseCase(workManagerRepository)

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
            todoItemUseCase.getTodoListLocal().collect {
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
            todoItemUseCase.getTodoListRemote().collect {
                if (it.status == ResultStatus.SUCCESS) {
                    val response = it.data
                    val replaceResult =
                        todoItemUseCase.replaceTodoListLocal(response.first).first { it.status != ResultStatus.LOADING }
                    if (replaceResult.status == ResultStatus.SUCCESS) {
                        revisionUseCase.setRevision(response.second)
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
                todoItemUseCase.deleteTodoItemLocal(todoItem.id).first { it.status != ResultStatus.LOADING }
            val deletedItem = if (deleteResult.status == ResultStatus.SUCCESS) {
                deleteResult.data.first[0]
            } else {
                throw RuntimeException("БД не может удалить запись todoItem: $todoItem")
            }
            if (snackBarBlock(deletedItem)) {
                val addResult =
                    todoItemUseCase.addTodoItemLocal(deletedItem).first { it.status != ResultStatus.LOADING }
                if (addResult.status != ResultStatus.SUCCESS) throw RuntimeException("БД не может добавить todoItem: $deletedItem")
            } else {
                todoItemUseCase.deleteTodoItemRemote(deletedItem.id).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        revisionUseCase.setRevision(response.second)
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
                todoItemUseCase.editTodoItemLocal(newTodoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.editTodoItemRemote(newTodoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        revisionUseCase.setRevision(response.second)
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

    fun setupCheckSynchronizedWorker(){
        workManagerUseCase.setupCheckSynchronizedWorker()
    }

    val isAuth: Boolean
        get() {
            val tokenPair = tokenUseCase.getTokenPair()
            return if (tokenPair == null) {
                logout()
                false
            } else true
        }

    private fun logout() {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout()
        }
    }

    var isViewFinished = MutableStateFlow(true)
}
