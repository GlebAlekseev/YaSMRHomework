package com.glebalekseevjk.yasmrhomework.data.remote

import androidx.lifecycle.asFlow
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.feature.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.feature.SynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

class SynchronizedInterceptor(
    private val synchronizedStorage: SynchronizedStorage,
    private val revisionStorage: RevisionStorage,
    private val todoService: TodoService,
    private val todoItemDao: TodoItemDao,
    private val mapper: Mapper<TodoItem, TodoItemDbModel>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!synchronizedStorage.getSynchronizedStatus()) {
            val localList = runBlocking {
                todoItemDao.getAll().asFlow().first()
            }.map { mapper.mapDbModelToItem(it) }
            val patchResult = runCatching {
                    todoService.patchTodoList(localList).execute()
                }.getOrNull()
            if (patchResult != null && patchResult.code() == 200) {
                val body = patchResult.body()
                revisionStorage.setRevision(Revision(body!!.revision))
                synchronizedStorage.setSynchronizedStatus(
                    SharedPreferencesSynchronizedStorage.SYNCHRONIZED
                )
            }else if (patchResult != null && patchResult.code() == 400){
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