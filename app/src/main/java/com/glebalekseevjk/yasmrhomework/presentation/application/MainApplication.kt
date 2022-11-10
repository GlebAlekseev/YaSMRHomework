package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.local.AppDatabase
import com.glebalekseevjk.yasmrhomework.data.mapper.TodoItemMapperImpl
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(
            sharedPreferencesTokenStorage,
            sharedPreferencesRevisionStorage,
            sharedPreferencesSynchronizedStorage,
            appDatabase.todoItemDao()
        )
    }

    private val sharedPreferencesTokenStorage by lazy {
        SharedPreferencesTokenStorage(this)
    }
    private val sharedPreferencesRevisionStorage by lazy {
        SharedPreferencesRevisionStorage(this)
    }
    private val sharedPreferencesSynchronizedStorage by lazy {
        SharedPreferencesSynchronizedStorage(this)
    }

    private val appDatabase = AppDatabase.getDataBase(this)

    private val todoItemMapperImpl = TodoItemMapperImpl()
    private val todoListRepositoryImpl = TodoListRepositoryImpl(
        appDatabase.todoItemDao(),
        todoItemMapperImpl,
        RetrofitClient.todoApi
    )
    private val authRepositoryImpl =
        AuthRepositoryImpl(RetrofitClient.authApi, sharedPreferencesTokenStorage)

    val todoViewModelFactory = TodoViewModelFactory(this, todoListRepositoryImpl)
    val todoListViewModelFactory = TodoListViewModelFactory(this, todoListRepositoryImpl)
    val mainViewModelFactory = MainViewModelFactory(
        this,
        authRepositoryImpl,
        sharedPreferencesTokenStorage,
        sharedPreferencesRevisionStorage
    )
}