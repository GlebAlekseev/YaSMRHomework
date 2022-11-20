package com.glebalekseevjk.yasmrhomework.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.interactor.GetTodoListUseCase
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication


class RefreshTodoWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val mainApplication = appContext as MainApplication
            val getTodoListUseCase = GetTodoListUseCase(mainApplication.todoListRepositoryImpl)
            var status = Result.success()
            getTodoListUseCase().collect {
                if (it.status != ResultStatus.SUCCESS && it.status != ResultStatus.LOADING) {
                    status = Result.failure()
                }
                if (it.status != ResultStatus.LOADING) return@collect
            }
            return status
        } catch (err: Exception) {
            return Result.failure()
        }
    }
    companion object {
        const val WORK_NAME = "RefreshTodoWorker"
    }
}