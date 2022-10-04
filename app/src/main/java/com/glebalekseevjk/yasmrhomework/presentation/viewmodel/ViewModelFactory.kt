package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl


class TodoViewModelFactory(
    private val todoListRepositoryImpl: TodoListRepositoryImpl,
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(
            todoListRepositoryImpl
        ) as T
    }
}

class TodoListViewModelFactory(
    private val todoListRepositoryImpl: TodoListRepositoryImpl,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(
            todoListRepositoryImpl
        ) as T
    }
}