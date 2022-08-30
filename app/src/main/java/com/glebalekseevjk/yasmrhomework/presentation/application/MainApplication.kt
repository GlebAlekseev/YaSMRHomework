package com.glebalekseevjk.yasmrhomework.presentation.application

import MainViewModelFactory
import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel

class MainApplication: Application() {
    val repositoryImpl = TodoItemsRepositoryImpl()
    val mainViewModelFactory = MainViewModelFactory(repositoryImpl)
    override fun onCreate() {
        super.onCreate()
    }
}