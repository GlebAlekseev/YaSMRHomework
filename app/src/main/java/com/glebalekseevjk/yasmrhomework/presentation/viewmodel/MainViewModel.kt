package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.*
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class MainViewModel(private val todoItemsRepositoryImpl: TodoItemsRepositoryImpl): ViewModel() {
    private val addTodoItemUseCase = AddTodoItemUseCase(todoItemsRepositoryImpl)
    private val editTodoItemUseCase = EditTodoItemUseCase(todoItemsRepositoryImpl)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoItemsRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoItemsRepositoryImpl)

    fun getTodo(id: String): TodoItem? {
        return getTodoItemUseCase(id)
    }

    fun getTodoList(): LiveData<List<TodoItem>> =
        getTodoListUseCase().asLiveData()

    suspend fun addTodo(todoItem: TodoItem){
        addTodoItemUseCase(todoItem)
    }
    suspend fun deleteTodo(todoItem: TodoItem){
        deleteTodoItemUseCase(todoItem)
    }
    suspend fun deleteTodo(todoId: String){
        deleteTodoItemUseCase(todoId)
    }
    suspend fun editTodo(todoItem: TodoItem){
        editTodoItemUseCase(todoItem)
    }
    suspend fun finishTodo(todoItem: TodoItem){
        val newTodoItem = todoItem.copy(finished = true)
        editTodoItemUseCase(newTodoItem)
    }

    val isViewFinishedLiveData: MutableLiveData<Boolean> = MutableLiveData(true)
    var isViewFinished: Boolean
        get() = isViewFinishedLiveData.value!!
        set(value) {
            isViewFinishedLiveData.value = value
            getTodoList().distinctUntilChanged()
        }

    private val _currentTodoItem: MutableLiveData<TodoItem> = MutableLiveData(TodoItem.DEFAULT)
    var currentTodoItem: TodoItem
        get() = _currentTodoItem.value!!
        set(value) {
            _currentTodoItem.value = value
        }
}