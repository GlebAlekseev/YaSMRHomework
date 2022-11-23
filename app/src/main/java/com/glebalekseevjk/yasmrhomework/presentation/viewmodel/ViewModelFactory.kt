package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRemoteRepositoryImpl


class TodoViewModelFactory(
    private val application: Application,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    private val todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(
            application,
            authRepositoryImpl,
            todoListLocalRepositoryImpl,
            todoListRemoteRepositoryImpl
        ) as T
    }
}

class TodoListViewModelFactory(
    private val application: Application,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl,
    private val todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(
            application,
            authRepositoryImpl,
            todoListLocalRepositoryImpl,
            todoListRemoteRepositoryImpl
        ) as T
    }
}

class MainViewModelFactory(
    private val application: Application,
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            application,
            authRepositoryImpl,
        ) as T
    }
}