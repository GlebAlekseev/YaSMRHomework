package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import androidx.work.*
import com.glebalekseevjk.yasmrhomework.data.worker.CheckSynchronizeWorker
import com.glebalekseevjk.yasmrhomework.data.worker.RefreshTodoWorker
import com.glebalekseevjk.yasmrhomework.domain.repository.SchedulerRepository
import java.util.concurrent.TimeUnit

class SchedulerRepositoryImpl(context: Context): SchedulerRepository {
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    override fun setupPeriodicTimeRefreshTodo(){
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

    override fun setupOneTimeCheckSynchronize(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val oneTimeRequest = OneTimeWorkRequestBuilder<CheckSynchronizeWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            CheckSynchronizeWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            oneTimeRequest
        )
    }
}