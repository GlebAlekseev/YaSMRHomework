package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl


class TodoViewModelFactory(
    private val application: Application,
    private val todoListRepositoryImpl: TodoListRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(
            application,
            todoListRepositoryImpl
        ) as T
    }
}

class TodoListViewModelFactory(
    private val application: Application,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val todoListRepositoryImpl: TodoListRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(
            application,
            authRepositoryImpl,
            todoListRepositoryImpl
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