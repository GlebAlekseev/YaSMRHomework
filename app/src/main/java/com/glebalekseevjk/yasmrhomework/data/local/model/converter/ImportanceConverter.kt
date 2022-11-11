package com.glebalekseevjk.yasmrhomework.data.local.model.converter

import androidx.room.TypeConverter
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel.Companion.Importance

class ImportanceConverter {

    @TypeConverter
    fun fromPriority(importance: Importance): String {
        return importance.name
    }

    @TypeConverter
    fun toPriority(importance: String): Importance {
        return Importance.valueOf(importance)
    }
}