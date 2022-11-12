package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.local.AppDatabase
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.mapper.TodoItemMapperImpl
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModelFactory

class MainApplication : Application() {
    private lateinit var appDatabase: AppDatabase
    private lateinit var todoItemMapperImpl: Mapper<TodoItem,TodoItemDbModel>

    private lateinit var todoListRepositoryImpl: TodoListRepositoryImpl
    private lateinit var authRepositoryImpl: AuthRepositoryImpl

    lateinit var todoViewModelFactory: TodoViewModelFactory
    lateinit var todoListViewModelFactory: TodoListViewModelFactory
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var sharedPreferencesTokenStorage: SharedPreferencesTokenStorage
    private lateinit var sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage
    private lateinit var sharedPreferencesSynchronizedStorage: SharedPreferencesSynchronizedStorage

    override fun onCreate() {
        super.onCreate()
        sharedPreferencesTokenStorage = SharedPreferencesTokenStorage(this)
        sharedPreferencesRevisionStorage = SharedPreferencesRevisionStorage(this)
        sharedPreferencesSynchronizedStorage = SharedPreferencesSynchronizedStorage(this)

        appDatabase = AppDatabase.getDataBase(this)
        todoItemMapperImpl = TodoItemMapperImpl()

        RetrofitClient.init(
            sharedPreferencesTokenStorage,
            sharedPreferencesRevisionStorage,
            sharedPreferencesSynchronizedStorage,
            appDatabase.todoItemDao()
        )

        todoListRepositoryImpl = TodoListRepositoryImpl(
            appDatabase.todoItemDao(),
            todoItemMapperImpl,
            RetrofitClient.todoApi,
            sharedPreferencesRevisionStorage
        )
        authRepositoryImpl = AuthRepositoryImpl(RetrofitClient.authApi, sharedPreferencesTokenStorage)

        todoViewModelFactory = TodoViewModelFactory(this, todoListRepositoryImpl)
        todoListViewModelFactory = TodoListViewModelFactory(this, authRepositoryImpl, todoListRepositoryImpl)
        mainViewModelFactory = MainViewModelFactory(
            this,
            authRepositoryImpl
        )
    }
}