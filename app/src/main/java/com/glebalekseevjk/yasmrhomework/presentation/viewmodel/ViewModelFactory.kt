package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl


class TodoViewModelFactory(private val repositoryImpl: TodoItemsRepositoryImpl): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(repositoryImpl) as T
    }
}

class TodoListViewModelFactory(private val repositoryImpl: TodoItemsRepositoryImpl): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(repositoryImpl) as T
    }
}