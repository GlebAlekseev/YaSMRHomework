package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem


class TodoViewModel(todoItemsRepositoryImpl: TodoItemsRepositoryImpl): BaseViewModel(todoItemsRepositoryImpl){
    var currentTodoItem: TodoItem = TodoItem.DEFAULT
}