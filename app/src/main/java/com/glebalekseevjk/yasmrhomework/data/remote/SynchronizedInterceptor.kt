package com.glebalekseevjk.yasmrhomework.data.remote

import androidx.lifecycle.asFlow
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronize.SynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

// Patch данных, по типу

class SynchronizedInterceptor(
    private val synchronizedStorage: SynchronizedStorage,
    private val revisionStorage: RevisionStorage,
    private val todoService: TodoService,
    private val todoItemDao: TodoItemDao,
    private val mapper: Mapper<TodoItem, TodoItemDbModel>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Если synchronizedStorage, то делаю сначала запрос на патч, иначе передаю дальше
        // Если не синхр.
        if (!synchronizedStorage.getSynchronizedStatus()) {
            // Отправляю запрос на patch with dataDb, получаю ответ, устанавливаю ревизию, данные, чищу синхр.
            val localList = runBlocking {
                todoItemDao.getAll().asFlow().first()
            }.map { mapper.mapDbModelToItem(it) }
            val patchResult = runCatching {
                    todoService.patchTodoList(localList).execute()
                }.getOrNull()
            if (patchResult != null && patchResult.code() == 200) {
                val body = patchResult.body()
                revisionStorage.setRevision(Revision(body!!.revision))
                // set list in db
                synchronizedStorage.setSynchronizedStatus(
                    SharedPreferencesSynchronizedStorage.SYNCHRONIZED
                )
            }else if (patchResult != null && patchResult?.code() == 400){
                synchronizedStorage.setSynchronizedStatus(
                    SharedPreferencesSynchronizedStorage.SYNCHRONIZED
                )
            } else {
                return Response.Builder()
                    .code(600)
                    .protocol(Protocol.HTTP_2)
                    .request(chain.request())
                    .build()
            }
        }
        return chain.proceed(chain.request())
    }
}