package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.*
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*


class TodoListViewModel(todoItemsRepositoryImpl: TodoItemsRepositoryImpl): BaseViewModel(todoItemsRepositoryImpl){
    var isViewFinished: Boolean = true
        set(value) {
            field = value
            updateTodoList()
        }
}