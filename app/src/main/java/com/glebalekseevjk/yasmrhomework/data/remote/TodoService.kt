package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoListResponse
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import retrofit2.Call
import retrofit2.http.*

interface TodoService {
    @GET("api/list")
    fun getTodoList(): Call<TodoListResponse>

    @PATCH("api/list")
    fun patchTodoList(@Body todoList: List<TodoItem>): Call<TodoListResponse>

    @GET("api/list/{id}")
    fun getTodoItem(@Path("id") todoId: Long): Call<TodoListResponse>

    @POST("api/list")
    fun addTodoItem(@Body todoItem: TodoItem): Call<TodoListResponse>

    @PUT("api/list/{id}")
    fun putTodoItem(
        @Path("id") todoId: Long,
        @Body todoItem: TodoItem
    ): Call<TodoListResponse>

    @DELETE("api/list/{id}")
    fun deleteTodoItem(@Path("id") todoId: Long): Call<TodoListResponse>
}