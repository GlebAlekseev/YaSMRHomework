package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
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

class MainListViewModelFactory(
    private val application: Application,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage,
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            application,
            authRepositoryImpl,
            sharedPreferencesTokenStorage,
            sharedPreferencesRevisionStorage
        ) as T
    }
}