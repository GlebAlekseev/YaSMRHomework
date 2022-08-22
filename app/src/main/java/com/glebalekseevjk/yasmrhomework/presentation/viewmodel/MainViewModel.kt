package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import java.util.concurrent.Flow

class MainViewModel: ViewModel() {
    private val todoItemsRepositoryImpl = TodoItemsRepositoryImpl()
    private val addTodoItemUseCase = AddTodoItemUseCase(todoItemsRepositoryImpl)
    private val editTodoItemUseCase = EditTodoItemUseCase(todoItemsRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoItemsRepositoryImpl)

    fun getTodo(id: String): TodoItem? {
        return getTodoItemUseCase(id)
    }
    fun getTodoList(): LiveData<List<TodoItem>>{
        return getTodoListUseCase()
    }
    fun addTodo(todoItem: TodoItem){
        addTodoItemUseCase(todoItem)
    }
    fun deleteTodo(todoItem: TodoItem){
        deleteTodoItemUseCase(todoItem)
    }
    fun editTodo(todoItem: TodoItem){
        editTodoItemUseCase(todoItem)
    }
    fun finishTodo(todoItem: TodoItem){
        val newTodoItem = todoItem.copy(finished = true)
        editTodoItemUseCase(newTodoItem)
    }


//    private val _isViewFinished

}