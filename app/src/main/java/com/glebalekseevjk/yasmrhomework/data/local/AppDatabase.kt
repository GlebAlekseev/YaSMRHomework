package com.glebalekseevjk.yasmrhomework.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.local.model.converter.ImportanceConverter

@Database(entities = [TodoItemDbModel::class], version = 1, exportSchema = false)
@TypeConverters(ImportanceConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "todoitem-database"
    }

    abstract fun todoItemDao(): TodoItemDao
}