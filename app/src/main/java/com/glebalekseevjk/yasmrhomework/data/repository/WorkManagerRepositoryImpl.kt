package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import androidx.work.*
import com.glebalekseevjk.yasmrhomework.data.worker.CheckSynchronizedWorker
import com.glebalekseevjk.yasmrhomework.data.worker.RefreshTodoWorker
import java.util.concurrent.TimeUnit

class WorkManagerRepositoryImpl(context: Context) {
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    fun setupRefreshTodoWorker(){
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