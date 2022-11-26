package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.repository.*


class TodoViewModelFactory(
    private val authRepository: AuthRepository,
    private val revisionRepository: RevisionRepository,
    private val tokenRepository: TokenRepository,
    private val todoListLocalRepository: TodoListLocalRepository,
    private val todoListRemoteRepository: TodoListRemoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(
            authRepository,
            tokenRepository,
            revisionRepository,
            todoListLocalRepository,
            todoListRemoteRepository
        ) as T
    }
}

class TodoListViewModelFactory(
    private val authRepository: AuthRepository,
    private val revisionRepository: RevisionRepository,
    private val tokenRepository: TokenRepository,
    private val todoListLocalRepository: TodoListLocalRepository,
    private val todoListRemoteRepository: TodoListRemoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoListViewModel(
            authRepository,
            tokenRepository,
            revisionRepository,
            todoListLocalRepository,
            todoListRemoteRepository
        ) as T
    }
}

class MainViewModelFactory(
    private val authRepository: AuthRepository,
    private val revisionRepository: RevisionRepository,
    private val tokenRepository: TokenRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            authRepository,
            tokenRepository,
            revisionRepository
        ) as T
    }
}