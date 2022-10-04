package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.local.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.AuthRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.remote.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication: Application() {
    private val todoListLocalRepositoryImpl = TodoListLocalRepositoryImpl()
    private val todoListRemoteRepositoryImpl = TodoListRemoteRepositoryImpl()
    private val authRemoteRepositoryImpl = AuthRemoteRepositoryImpl()

    val todoViewModelFactory = TodoViewModelFactory(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )
    val todoListViewModelFactory = TodoListViewModelFactory(
        todoListLocalRepositoryImpl,
        todoListRemoteRepositoryImpl,
        authRemoteRepositoryImpl
    )
}