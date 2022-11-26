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
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.first


class TodoViewModel(
    authRepository: AuthRepository,
    tokenRepository: TokenRepository,
    revisionRepository: RevisionRepository,
    todoListLocalRepository: TodoListLocalRepository,
    todoListRemoteRepository: TodoListRemoteRepository,
    workManagerRepository: WorkManagerRepository
) : BaseViewModel() {
    private val authUseCase = AuthUseCase(authRepository)
    private val revisionUseCase = RevisionUseCase(revisionRepository)
    private val tokenUseCase = TokenUseCase(tokenRepository)
    private val todoItemUseCase = TodoItemUseCase(todoListLocalRepository, todoListRemoteRepository)
    private val workManagerUseCase = WorkManagerUseCase(workManagerRepository)

    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    fun setCurrentTodoItemById(todoId: Long) {
        runBlockingWithExceptionHandler {
            val getResult =
                todoItemUseCase.getTodoItemLocal(todoId).first { it.status != ResultStatus.LOADING }
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
                todoItemUseCase.addTodoItemLocal(todoItem).first { it.status != ResultStatus.LOADING }
            if (addResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.addTodoItemRemote(todoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        revisionUseCase.setRevision(response.second)
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
                todoItemUseCase.editTodoItemLocal(todoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.editTodoItemRemote(todoItem).collect {
                    if (it.status == ResultStatus.SUCCESS) {
                        val response = it.data
                        revisionUseCase.setRevision(response.second)
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
                    }else{
                        block(Result(it.status, TodoItem.PLUG, it.message))
                    }
                }
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

    var currentTodoItem: TodoItem = TodoItem.PLUG
}
