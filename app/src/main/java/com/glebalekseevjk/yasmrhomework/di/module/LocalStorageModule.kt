package com.glebalekseevjk.yasmrhomework.di.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.yasmrhomework.data.local.AppDatabase
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.mapper.TodoItemMapperImpl
import com.glebalekseevjk.yasmrhomework.di.scope.AppComponentScope
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface LocalStorageModule {
    @AppComponentScope
    @Binds
    fun provideMapperTodoItem(todoItemMapperImpl: TodoItemMapperImpl): Mapper<TodoItem, TodoItemDbModel>

    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }
        @AppComponentScope
        @Provides
        fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
            return appDatabase.todoItemDao()
        }
    }
}
