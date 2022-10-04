package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.local.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.AuthRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.TodoListRemoteRepositoryImpl


class TodoViewModelFactory(
    private val todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    private val todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
    private val authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(
            todoListLocalRepositoryImpl,
            todoListRemoteRepositoryImpl,
            authRemoteRepositoryImpl
        ) as T
    }
}

class TodoListViewModelFactory(
    private val todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    private val todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
    private val authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(
            todoListLocalRepositoryImpl,
            todoListRemoteRepositoryImpl,
            authRemoteRepositoryImpl
        ) as T
    }
}