package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.remote.model.RefreshToken
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronized.SynchronizedStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.awaitResponse

// Patch данных, по типу

class SynchronizedInterceptor(
    private val synchronizedStorage: SynchronizedStorage,
    private val revisionStorage: RevisionStorage,
    private val todoService: TodoService,
    private val todoItemDao: TodoItemDao
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Если synchronizedStorage, то делаю сначала запрос на патч, иначе передаю дальше

        // Если не синхр.
        if (!synchronizedStorage.getSynchronizedStatus()){
            // Отправляю запрос на patch with dataDb, получаю ответ, устанавливаю ревизию, данные, чищу синхр.
            runBlocking {
                runCatching {
                    todoService.patchTodoList(emptyList()).awaitResponse()
                }.getOrNull()?.let {
                    if (it.code() == 200){
                        val body = it.body()
                        revisionStorage.setRevision(Revision(it.body()!!.revision))
                        // set list in db
                        synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.SYNCHRONIZED)
                    }
                }
            }

        }
        return chain.proceed(chain.request())
    }
}