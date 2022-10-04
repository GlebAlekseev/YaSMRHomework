package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.*
import com.glebalekseevjk.yasmrhomework.data.local.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.AuthRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.TodoListRemoteRepositoryImpl
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
    todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
    authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
    ): BaseViewModel(){
    private val editTodoItemUseCase = EditTodoItemUseCase(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )
    private val getTodoListUseCase = GetTodoListUseCase(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler{ coroutineContext, exception ->
        val message = ExceptionHandler.parse(exception)
        _errorHandler.value = message
        when(coroutineContext){
            getTodoListJob -> {
                _todoListViewState.value = _todoListViewState.value.copy(errorMessage = message)
            }
        }
    }

    private var getTodoListJob: Job? = null

    private var _todoListViewState = MutableStateFlow(TodoListViewState.DEFAULT)
    val todoListViewState: StateFlow<TodoListViewState>
        get() {
            getTodoListJob = viewModelScope.launchCoroutine {
                loadTodoList()
            }
            return _todoListViewState
        }

    private suspend fun loadTodoList(){
        getTodoListUseCase().collect{
            _todoListViewState.value = _todoListViewState.value!!.copy(result = it)
        }
    }

    fun deleteTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        viewModelScope.launchCoroutine {
            deleteTodoItemUseCase(todoItem).collect{
                block(it)
            }
        }
    }

    fun finishTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        val newTodoItem = todoItem.copy(finished = true)
        viewModelScope.launchCoroutine {
            editTodoItemUseCase(newTodoItem).collect{
                block(it)
            }
        }
    }
    var isViewFinished = MutableStateFlow(true)
}
