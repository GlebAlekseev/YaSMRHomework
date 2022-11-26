package com.glebalekseevjk.yasmrhomework.ui.application

import android.app.Application
import androidx.work.*
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.local.AppDatabase
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.mapper.TodoItemMapperImpl
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.repository.*
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.MainViewModelFactory
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoListViewModelFactory
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModelFactory
import com.glebalekseevjk.yasmrhomework.ui.worker.CheckSynchronizedWorker
import com.glebalekseevjk.yasmrhomework.ui.worker.RefreshTodoWorker
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
    private lateinit var appDatabase: AppDatabase
    private lateinit var todoItemMapperImpl: Mapper<TodoItem,TodoItemDbModel>

    lateinit var todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl
    lateinit var todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl
    lateinit var authRepositoryImpl: AuthRepositoryImpl
    lateinit var revisionRepositoryImpl: RevisionRepositoryImpl
    lateinit var synchronizedRepositoryImpl: SynchronizedRepositoryImpl
    lateinit var tokenRepositoryImpl: TokenRepositoryImpl

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
        todoListLocalRepositoryImpl = TodoListLocalRepositoryImpl(
            appDatabase.todoItemDao(),
            todoItemMapperImpl,
            sharedPreferencesRevisionStorage
        )
        todoListRemoteRepositoryImpl = TodoListRemoteRepositoryImpl(
            RetrofitClient.todoApi
        )
        authRepositoryImpl = AuthRepositoryImpl(
            appDatabase.todoItemDao(),
            RetrofitClient.authApi,
            sharedPreferencesTokenStorage,
            sharedPreferencesRevisionStorage,
            sharedPreferencesSynchronizedStorage
        )
        revisionRepositoryImpl = RevisionRepositoryImpl(this)
        synchronizedRepositoryImpl = SynchronizedRepositoryImpl(this)
        tokenRepositoryImpl = TokenRepositoryImpl(this)

        todoViewModelFactory = TodoViewModelFactory(authRepositoryImpl, revisionRepositoryImpl, tokenRepositoryImpl, todoListLocalRepositoryImpl, todoListRemoteRepositoryImpl)
        todoListViewModelFactory = TodoListViewModelFactory(authRepositoryImpl, revisionRepositoryImpl, tokenRepositoryImpl, todoListLocalRepositoryImpl, todoListRemoteRepositoryImpl)
        mainViewModelFactory = MainViewModelFactory(authRepositoryImpl, revisionRepositoryImpl, tokenRepositoryImpl)
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