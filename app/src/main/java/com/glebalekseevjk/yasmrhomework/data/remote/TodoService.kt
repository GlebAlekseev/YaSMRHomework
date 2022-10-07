package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoItemResponse
import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoListResponse
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import retrofit2.Call
import retrofit2.http.*

interface TodoService {
    @GET("api/list")
    suspend fun getTodoList(): Call<TodoListResponse>

    @PATCH("api/list")
    suspend fun patchTodoList(): Call<TodoListResponse>

    @GET("api/list/{id}")
    suspend fun getTodoItem(@Path("id") todoId: Long): Call<TodoItemResponse>

    @POST("api/list")
    suspend fun addTodoItem(@Body todoItem: TodoItem): Call<TodoItemResponse>

    @PUT("api/list/{id}")
    suspend fun putTodoItem(@Path("id") todoId: Long,@Body todoItem: TodoItem): Call<TodoItemResponse>

    @DELETE("api/list/{id}")
    suspend fun deleteTodoItem(@Path("id") todoId: Long): Call<TodoItemResponse>
}