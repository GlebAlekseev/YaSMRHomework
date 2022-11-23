package com.glebalekseevjk.yasmrhomework.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.interactor.AddTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.DeleteTodoItemUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.DeleteTodoListUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.GetTodoListUseCase
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import kotlinx.coroutines.flow.first


class RefreshTodoWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {



    override suspend fun doWork(): Result {
        return try {
            val mainApplication = appContext as MainApplication
            val getTodoListRemoteUseCase = GetTodoListUseCase(mainApplication.todoListRemoteRepositoryImpl)
            val deleteTodoListLocalUseCase = DeleteTodoListUseCase(mainApplication.todoListLocalRepositoryImpl)
            val addTodoItemLocalUseCase = AddTodoItemUseCase(mainApplication.todoListLocalRepositoryImpl)

            var status = Result.success()
            getTodoListRemoteUseCase().collect {
                if (it.status == ResultStatus.SUCCESS) {
                    val response = it.data
                    val deleteResult =
                        deleteTodoListLocalUseCase().first { it.status != ResultStatus.LOADING }
                    if (deleteResult.status == ResultStatus.SUCCESS) {
                        response.first.forEach {
                            if (addTodoItemLocalUseCase(it).first { it.status != ResultStatus.LOADING }.status != ResultStatus.SUCCESS) {
                                throw RuntimeException("БД не может добавить запись")
                            }
                        }
                        mainApplication.sharedPreferencesRevisionStorage.setRevision(response.second)
                    } else {
                        throw RuntimeException("БД не может удалить все записи")
                    }
                }
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