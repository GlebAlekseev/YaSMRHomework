package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.work.*
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
import com.glebalekseevjk.yasmrhomework.presentation.worker.CheckSynchronizedWorker
import com.glebalekseevjk.yasmrhomework.presentation.worker.RefreshTodoWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
    private lateinit var appDatabase: AppDatabase
    private lateinit var todoItemMapperImpl: Mapper<TodoItem,TodoItemDbModel>

    lateinit var todoListRepositoryImpl: TodoListRepositoryImpl
    private lateinit var authRepositoryImpl: AuthRepositoryImpl

    lateinit var todoViewModelFactory: TodoViewModelFactory
    lateinit var todoListViewModelFactory: TodoListViewModelFactory
    lateinit var mainViewModelFactory: MainViewModelFactory

    lateinit var sharedPreferencesTokenStorage: SharedPreferencesTokenStorage
    lateinit var sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage
    lateinit var sharedPreferencesSynchronizedStorage: SharedPreferencesSynchronizedStorage

    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

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
            appDatabase.todoItemDao(),
            todoItemMapperImpl
        )

        todoListRepositoryImpl = TodoListRepositoryImpl(
            appDatabase.todoItemDao(),
            todoItemMapperImpl,
            RetrofitClient.todoApi,
            sharedPreferencesRevisionStorage,
            sharedPreferencesSynchronizedStorage
        )
        authRepositoryImpl = AuthRepositoryImpl(RetrofitClient.authApi, sharedPreferencesTokenStorage)

        todoViewModelFactory = TodoViewModelFactory(this, todoListRepositoryImpl)
        todoListViewModelFactory = TodoListViewModelFactory(this, authRepositoryImpl, todoListRepositoryImpl)
        mainViewModelFactory = MainViewModelFactory(
            this,
            authRepositoryImpl
        )

        setupWorkers()
    }

    private fun setupWorkers(){
        setupRefreshTodoWorker()
    }

    private fun setupRefreshTodoWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshTodoWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            RefreshTodoWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    fun setupCheckSynchronizedWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val oneTimeRequest = OneTimeWorkRequestBuilder<CheckSynchronizedWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            CheckSynchronizedWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            oneTimeRequest
        )
    }
}