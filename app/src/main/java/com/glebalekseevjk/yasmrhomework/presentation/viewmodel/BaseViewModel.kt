package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import kotlinx.coroutines.flow.distinctUntilChanged

abstract class BaseViewModel(private val todoItemsRepositoryImpl: TodoItemsRepositoryImpl): ViewModel() {
    private val addTodoItemUseCase = AddTodoItemUseCase(todoItemsRepositoryImpl)
    private val editTodoItemUseCase = EditTodoItemUseCase(todoItemsRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoItemsRepositoryImpl)

    val todoList: LiveData<List<TodoItem>> = getTodoListUseCase().asLiveData()
    fun updateTodoList() = getTodoListUseCase().distinctUntilChanged()

    fun getTodo(id: String): TodoItem? {
        return getTodoItemUseCase(id)
    }
    fun addTodo(todoItem: TodoItem){
        addTodoItemUseCase(todoItem)
    }
    fun deleteTodo(todoItem: TodoItem){
        deleteTodoItemUseCase(todoItem)
    }
    fun deleteTodo(todoId: String){
        deleteTodoItemUseCase(todoId)
    }
    fun editTodo(todoItem: TodoItem){
        editTodoItemUseCase(todoItem)
    }
    fun finishTodo(todoItem: TodoItem){
        val newTodoItem = todoItem.copy(finished = true)
        editTodoItemUseCase(newTodoItem)
    }
}
