package com.glebalekseevjk.yasmrhomework.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.interactor.TodoItemUseCase
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import kotlinx.coroutines.flow.first

class RefreshTodoWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val mainApplication = appContext as MainApplication
            val todoItemUseCase = TodoItemUseCase(
                mainApplication.todoListLocalRepositoryImpl,
                mainApplication.todoListRemoteRepositoryImpl
            )

            var status = Result.success()
            todoItemUseCase.getTodoListRemote().collect { result ->
                if (result.status == ResultStatus.SUCCESS) {
                    val response = result.data
                    val deleteResult =
                        todoItemUseCase.deleteTodoListLocal()
                            .first { it.status != ResultStatus.LOADING }
                    if (deleteResult.status == ResultStatus.SUCCESS) {
                        response.first.forEach { data ->
                            if (todoItemUseCase.addTodoItemLocal(data)
                                    .first { it.status != ResultStatus.LOADING }.status != ResultStatus.SUCCESS) {
                                throw RuntimeException("БД не может добавить запись")
                            }
                        }
                        mainApplication.sharedPreferencesRevisionStorage.setRevision(response.second)
                    } else {
                        throw RuntimeException("БД не может удалить все записи")
                    }
                }
                if (result.status != ResultStatus.SUCCESS && result.status != ResultStatus.LOADING) {
                    status = Result.failure()
                }
                if (result.status != ResultStatus.LOADING) return@collect
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