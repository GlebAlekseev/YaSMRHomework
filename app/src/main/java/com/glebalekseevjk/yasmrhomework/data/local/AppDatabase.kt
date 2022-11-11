package com.glebalekseevjk.yasmrhomework.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.local.model.converter.ImportanceConverter

@Database(entities = [TodoItemDbModel::class], version = 1)
@TypeConverters(ImportanceConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "todoitem-database"
        fun getDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun todoItemDao(): TodoItemDao
}