package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModelFactory

class MainApplication: Application() {
    val repositoryImpl = TodoItemsRepositoryImpl()
    val mainViewModelFactory = MainViewModelFactory(repositoryImpl)
    override fun onCreate() {
        super.onCreate()
    }
}