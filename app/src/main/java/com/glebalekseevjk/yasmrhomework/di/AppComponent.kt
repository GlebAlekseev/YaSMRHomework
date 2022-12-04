package com.glebalekseevjk.yasmrhomework.di

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.worker.CheckSynchronizeWorker
import com.glebalekseevjk.yasmrhomework.data.worker.RefreshTodoWorker
import com.glebalekseevjk.yasmrhomework.di.module.LocalStorageModule
import com.glebalekseevjk.yasmrhomework.di.module.RemoteStorageModule
import com.glebalekseevjk.yasmrhomework.di.module.RepositoryModule
import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RepositoryModule::class, LocalStorageModule::class, RemoteStorageModule::class])
interface AppComponent {
    fun createMainActivitySubcomponent(): MainActivitySubcomponent
    fun createTodoFragmentSubComponent(): TodoFragmentSubcomponent
    fun createTodoListFragmentSubComponent(): TodoListFragmentSubcomponent
    fun injectMainApplication(application: MainApplication)
    fun injectCheckSynchronizeWorker(checkSynchronizeWorker: CheckSynchronizeWorker)
    fun injectRefreshTodoWorker(refreshTodoWorker: RefreshTodoWorker)

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}