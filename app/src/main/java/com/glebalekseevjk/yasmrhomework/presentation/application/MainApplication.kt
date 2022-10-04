package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication: Application() {
    private val todoListRepositoryImpl = TodoListRepositoryImpl()
    val todoViewModelFactory = TodoViewModelFactory(todoListRepositoryImpl)
    val todoListViewModelFactory = TodoListViewModelFactory(todoListRepositoryImpl)
}