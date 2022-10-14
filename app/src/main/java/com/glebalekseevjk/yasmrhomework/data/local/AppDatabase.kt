package com.glebalekseevjk.yasmrhomework.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel

@Database(entities = [TodoItemDbModel::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    companion object{
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