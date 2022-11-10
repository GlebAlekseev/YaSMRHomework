package com.glebalekseevjk.yasmrhomework.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItemDbModel(
    @ColumnInfo(name = "user_id") val userId: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "importance") val importance: Importance,
    @ColumnInfo(name = "deadline") val deadline: Long?,
    @ColumnInfo(name = "done") val done: Boolean,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "changed_at") val changedAt: Long?,
    @ColumnInfo(name = "lastUpdated_by") val lastUpdatedBy: Long,
) {
    companion object {
        enum class Importance {
            LOW,
            BASIC,
            IMPORTANT
        }
    }
}