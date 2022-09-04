package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking


class TodoViewModel(todoItemsRepositoryImpl: TodoItemsRepositoryImpl): BaseViewModel(){
    private val addTodoItemUseCase = AddTodoItemUseCase(todoItemsRepositoryImpl)
    private val editTodoItemUseCase = EditTodoItemUseCase(todoItemsRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoItemsRepositoryImpl)

    override val coroutineExceptionHandler = CoroutineExceptionHandler{ coroutineContext, exception ->
        val message = ExceptionHandler.parse(exception)
        _errorHandler.value = message
    }

    private var addTodoJob: Job? = null
    private var deleteTodoJob: Job? = null
    private var editTodoJob: Job? = null

    fun setCurrentTodoItemById(id: String){
        runBlocking {
            currentTodoItem = getTodoItemUseCase(id).last().data
        }
    }

    fun addTodo(todoItem: TodoItem){
        editTodoJob = launchCoroutine {
            addTodoItemUseCase(todoItem).collect{
                when(it.status){
                    ResultStatus.SUCCESS -> {
                        println("Добавлен элемент с id: ${it.data.id}")
                    }
                    ResultStatus.LOADING -> {
                        println("Добавление...")
                    }
                    ResultStatus.FAILURE -> {
                        println("Ошибка добавления элемента id: ${it.data.id}")
                    }
                }
            }
        }
    }

    fun editTodo(todoItem: TodoItem){
        editTodoJob = launchCoroutine {
            editTodoItemUseCase(todoItem).collect{
                when(it.status){
                    ResultStatus.SUCCESS -> {
                        println("Отредактирован элемент с id: ${it.data.id}")
                    }
                    ResultStatus.LOADING -> {
                        println("Редактирование...")
                    }
                    ResultStatus.FAILURE -> {
                        println("Ошибка редактирования элемента id: ${it.data.id}")
                    }
                }
            }
        }
    }

    fun deleteTodo(todoId: String){
        deleteTodoJob = launchCoroutine {
            deleteTodoItemUseCase(todoId).collect{
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

    var currentTodoItem: TodoItem = TodoItem.DEFAULT
    override fun onCleared() {
        super.onCleared()
        addTodoJob?.cancel()
        deleteTodoJob?.cancel()
        editTodoJob?.cancel()
    }
}
