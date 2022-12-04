package com.glebalekseevjk.yasmrhomework.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM TodoItemDbModel WHERE id = :todoId")
    fun get(todoId: Long): TodoItemDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoItemDbModel: TodoItemDbModel)

    @Query("DELETE FROM TodoItemDbModel WHERE id = :todoId")
    fun delete(todoId: Long)

    @Query("SELECT * FROM TodoItemDbModel ORDER BY created_at ASC")
    fun getAll(): LiveData<List<TodoItemDbModel>>

    @Query("DELETE FROM TodoItemDbModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(vararg todoList: TodoItemDbModel)

    @Transaction
    fun replaceAll(todoList: List<TodoItemDbModel>) {
        deleteAll()
        addAll(*todoList.toTypedArray())
    }
}