package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.*
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
    todoListRepositoryImpl: TodoListRepositoryImpl,
    ): BaseViewModel(){
    private val editTodoItemUseCase = EditTodoItemUseCase(todoListRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoListRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoListRepositoryImpl)

    override val coroutineExceptionHandler = CoroutineExceptionHandler{ coroutineContext, exception ->
        val message = ExceptionHandler.parse(exception)
        _errorHandler.value = message
        when(coroutineContext){
            getTodoListJob -> {
                _todoListViewState.value = _todoListViewState.value.copy()
            }
        }
    }

    private var getTodoListJob: Job? = null

    private var _todoListViewState = MutableStateFlow(TodoListViewState.PLUG)
    val todoListViewState: StateFlow<TodoListViewState>
        get() {
            getTodoListJob = viewModelScope.launchCoroutine {
                loadTodoList()
            }
            return _todoListViewState
        }

    private suspend fun loadTodoList(){
        getTodoListUseCase().collect{
            _todoListViewState.value = _todoListViewState.value.copy(result = it)
        }
    }

    fun deleteTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        viewModelScope.launchCoroutine {
            deleteTodoItemUseCase(todoItem.id).collect{
                block(it)
            }
        }
    }

    fun finishTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        val newTodoItem = todoItem.copy(done = true)
        viewModelScope.launchCoroutine {
            editTodoItemUseCase(newTodoItem).collect{
                block(it)
            }
        }
    }
    var isViewFinished = MutableStateFlow(true)
}
