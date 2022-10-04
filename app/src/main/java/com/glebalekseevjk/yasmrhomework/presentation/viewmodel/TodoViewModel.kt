package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.data.local.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.AuthRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking


class TodoViewModel(
    todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
    authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
): BaseViewModel(){
    private val addTodoItemUseCase = AddTodoItemUseCase(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )
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
    private val getTodoItemUseCase = GetTodoItemUseCase(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler{ coroutineContext, exception ->
        val message = ExceptionHandler.parse(exception)
        _errorHandler.value = message
    }

    fun setCurrentTodoItemById(id: String){
        runBlocking {
            currentTodoItem = getTodoItemUseCase(id).last().data
        }
    }

    fun addTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        viewModelScope.launchCoroutine {
            addTodoItemUseCase(todoItem).collect{
                block(it)
            }
        }
    }

    fun editTodo(todoItem: TodoItem, block: (Result<TodoItem>)->Unit){
        viewModelScope.launchCoroutine {
            editTodoItemUseCase(todoItem).collect{
                block(it)
            }
        }
    }

    fun deleteTodo(todoId: String, block: (Result<TodoItem>)->Unit){
        viewModelScope.launchCoroutine {
            deleteTodoItemUseCase(todoId).collect{
                block(it)
            }
        }
    }

    var currentTodoItem: TodoItem = TodoItem.DEFAULT
}
