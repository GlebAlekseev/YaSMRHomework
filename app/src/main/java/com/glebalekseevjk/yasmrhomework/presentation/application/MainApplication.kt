package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(sharedPreferencesTokenStorage, sharedPreferencesRevisionStorage)
    }
    private val sharedPreferencesTokenStorage by lazy{
        SharedPreferencesTokenStorage(this)
    }
    private val sharedPreferencesRevisionStorage by lazy {
        SharedPreferencesRevisionStorage(this)
    }
    private val todoListRepositoryImpl = TodoListRepositoryImpl()
    private val authRepositoryImpl = AuthRepositoryImpl()
    val todoViewModelFactory = TodoViewModelFactory(todoListRepositoryImpl)
    val todoListViewModelFactory = TodoListViewModelFactory(todoListRepositoryImpl)
    val mainViewModelFactory = MainListViewModelFactory(
        this,
        authRepositoryImpl,
        sharedPreferencesTokenStorage,
        sharedPreferencesRevisionStorage
    )
}