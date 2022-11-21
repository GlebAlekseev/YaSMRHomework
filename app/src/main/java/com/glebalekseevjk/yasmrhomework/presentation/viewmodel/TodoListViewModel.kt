package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoListViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
    todoListRepositoryImpl: TodoListRepositoryImpl,
) : BaseViewModel(application) {
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage = SharedPreferencesTokenStorage(application)
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage = SharedPreferencesRevisionStorage(application)

    private val editTodoItemUseCase = EditTodoItemUseCase(todoListRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoListRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoListRepositoryImpl)
    private val logoutUseCase = LogoutUseCase(authRepositoryImpl)
    private val synchronizeTodoListUseCase = SynchronizeTodoListUseCase(todoListRepositoryImpl)


    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = ExceptionHandler.parse(exception)
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

    private fun observeTodoList(){
        getTodoListJob = viewModelScope.launchWithExceptionHandler {
            getTodoListUseCase().collect {
                _todoListViewState.value = _todoListViewState.value.copy(result = it)
            }
        }
    }

    fun synchronizeTodoList(block: (Result<List<TodoItem>>) -> Unit){
        viewModelScope.launchWithExceptionHandler {
            synchronizeTodoListUseCase().collect {
                block(it)
            }
        }
    }

    fun deleteTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            deleteTodoItemUseCase(todoItem.id).collect {
                block(it)
            }
        }
    }

    fun finishTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        val newTodoItem = todoItem.copy(done = true)
        viewModelScope.launchWithExceptionHandler {
            editTodoItemUseCase(newTodoItem).collect {
                block(it)
            }
        }
    }

    fun logout(){
        viewModelScope.launchWithExceptionHandler {
            logoutUseCase()
        }
    }

    val isAuth: Boolean
        get() {
            val tokenPair = sharedPreferencesTokenStorage.getTokenPair()
            return if (tokenPair == null) {
                sharedPreferencesRevisionStorage.clear()
                // Удалить кеш записи в браузере
                false
            } else true
        }
    var isViewFinished = MutableStateFlow(true)
}
