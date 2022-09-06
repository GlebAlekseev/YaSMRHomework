package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.*
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.state.TodoListViewState
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged

class TodoListViewModel(todoItemsRepositoryImpl: TodoItemsRepositoryImpl): BaseViewModel(){
    private val editTodoItemUseCase = EditTodoItemUseCase(todoItemsRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoItemsRepositoryImpl)

    override val coroutineExceptionHandler = CoroutineExceptionHandler{ coroutineContext, exception ->
        val message = ExceptionHandler.parse(exception)
        _errorHandler.value = message
        when(coroutineContext){
            getTodoListJob -> {
                _todoListViewState.value = _todoListViewState.value?.copy(errorMessage = message)
            }
        }
    }
    private var getTodoListJob: Job? = null
    private var deleteTodoJob: Job? = null
    private var finishTodoJob: Job? = null


    private var _todoListViewState = MutableLiveData(TodoListViewState.DEFAULT)
    val todoListViewState: LiveData<TodoListViewState>
        get() {
            getTodoListJob = launchCoroutine {
                loadTodoList()
            }
            return _todoListViewState
        }

    private suspend fun loadTodoList(){
        getTodoListUseCase().collect{
            _todoListViewState.value = _todoListViewState.value!!.copy(result = it)
        }
    }

    private fun updateTodoList() {
        _todoListViewState.value = _todoListViewState.value
    }

    fun deleteTodo(todoItem: TodoItem){
        deleteTodoJob = launchCoroutine {
            deleteTodoItemUseCase(todoItem).collect{
                when(it.status){
                    ResultStatus.SUCCESS -> {
                        println("Удален элемент с id: ${it.data.id}")
                    }
                    ResultStatus.LOADING -> {
                        println("Удаление...")
                    }
                    ResultStatus.FAILURE -> {
                        println("Ошибка удаления элемента id: ${it.data.id}")
                    }
                }
            }
        }
    }
    fun finishTodo(todoItem: TodoItem){
        val newTodoItem = todoItem.copy(finished = true)
        finishTodoJob = launchCoroutine {
            editTodoItemUseCase(newTodoItem).collect{
                when(it.status){
                    ResultStatus.SUCCESS -> {
                        println("Завершен элемент с id: ${it.data.id}")
                    }
                    ResultStatus.LOADING -> {
                        println("Завершение...")
                    }
                    ResultStatus.FAILURE -> {
                        println("Ошибка завершения элемента id: ${it.data.id}")
                    }
                }
            }
        }

    }

    var isViewFinished: Boolean = true
        set(value) {
            field = value
            updateTodoList()
        }

    override fun onCleared() {
        super.onCleared()
        getTodoListJob?.cancel()
        deleteTodoJob?.cancel()
        finishTodoJob?.cancel()
    }
}
