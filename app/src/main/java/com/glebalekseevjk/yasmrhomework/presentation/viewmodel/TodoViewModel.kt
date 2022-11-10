package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.AddTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.DeleteTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.EditTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.GetTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.last


class TodoViewModel(
    application: Application,
    todoListRepositoryImpl: TodoListRepositoryImpl,
) : BaseViewModel(application) {
    private val addTodoItemUseCase = AddTodoItemUseCase(todoListRepositoryImpl)
    private val editTodoItemUseCase = EditTodoItemUseCase(todoListRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoListRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoListRepositoryImpl)

    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = ExceptionHandler.parse(exception)
            _errorHandler.value = message
        }

    fun setCurrentTodoItemById(todoId: Long) {
        runBlockingWithExceptionHandler {
            currentTodoItem = getTodoItemUseCase(todoId).last().data
        }
    }

    fun addTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            addTodoItemUseCase(todoItem).collect {
                block(it)
            }
        }
    }

    fun editTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            editTodoItemUseCase(todoItem).collect {
                block(it)
            }
        }
    }

    fun deleteTodo(todoId: Long, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            deleteTodoItemUseCase(todoId).collect {
                block(it)
            }
        }
    }

    var currentTodoItem: TodoItem = TodoItem.PLUG
}
