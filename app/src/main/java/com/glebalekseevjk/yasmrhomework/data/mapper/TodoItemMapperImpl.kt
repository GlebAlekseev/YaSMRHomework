package com.glebalekseevjk.yasmrhomework.data.mapper

import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.domain.entity.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import javax.inject.Inject


class TodoItemMapperImpl @Inject constructor() : Mapper<TodoItem, TodoItemDbModel> {
    override fun mapItemToDbModel(item: TodoItem): TodoItemDbModel {
        with(item) {
            return TodoItemDbModel(
                userId,
                id,
                text,
                when (importance) {
                    Importance.LOW -> TodoItemDbModel.Companion.Importance.LOW
                    Importance.BASIC -> TodoItemDbModel.Companion.Importance.BASIC
                    Importance.IMPORTANT -> TodoItemDbModel.Companion.Importance.IMPORTANT
                },
                deadline,
                done,
                color,
                createdAt,
                changedAt,
                lastUpdatedBy
            )
        }
    }

    override fun mapDbModelToItem(dbModel: TodoItemDbModel): TodoItem {
        with(dbModel) {
            return TodoItem(
                userId,
                id,
                text,
                when (importance) {
                    TodoItemDbModel.Companion.Importance.LOW -> Importance.LOW
                    TodoItemDbModel.Companion.Importance.BASIC -> Importance.BASIC
                    TodoItemDbModel.Companion.Importance.IMPORTANT -> Importance.IMPORTANT
                },
                deadline,
                done,
                color,
                createdAt,
                changedAt,
                lastUpdatedBy
            )
        }
    }
}