package com.glebalekseevjk.yasmrhomework.ui.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.data.repository.SchedulerRepositoryImpl
import com.glebalekseevjk.yasmrhomework.di.AppComponent
import com.glebalekseevjk.yasmrhomework.di.DaggerAppComponent
import javax.inject.Inject

class MainApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var schedulerRepositoryImpl: SchedulerRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        appComponent.injectMainApplication(this)
        setupWorkers()
    }

    private fun setupWorkers() {
        schedulerRepositoryImpl.setupPeriodicTimeRefreshTodo()
    }
}

