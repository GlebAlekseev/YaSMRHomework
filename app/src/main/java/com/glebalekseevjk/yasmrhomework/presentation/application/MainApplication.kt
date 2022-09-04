package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.local.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication: Application() {
    val repositoryImpl = TodoItemsRepositoryImpl()
    val todoViewModelFactory = TodoViewModelFactory(repositoryImpl)
    val todoListViewModelFactory = TodoListViewModelFactory(repositoryImpl)
}