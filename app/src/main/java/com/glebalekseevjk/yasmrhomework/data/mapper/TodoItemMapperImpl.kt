package com.glebalekseevjk.yasmrhomework.data.mapper

import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper


class TodoItemMapperImpl: Mapper<TodoItem, TodoItemDbModel> {
    override fun mapItemToDbModel(item: TodoItem): TodoItemDbModel {
        with(item){
            return TodoItemDbModel(
                userId,
                id,
                text,
                when(importance){
                    TodoItem.Companion.Importance.LOW -> TodoItemDbModel.Companion.Importance.LOW
                    TodoItem.Companion.Importance.BASIC -> TodoItemDbModel.Companion.Importance.BASIC
                    TodoItem.Companion.Importance.IMPORTANT -> TodoItemDbModel.Companion.Importance.IMPORTANT
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
        with(dbModel){
            return TodoItem(
                userId,
                id,
                text,
                when(importance){
                    TodoItemDbModel.Companion.Importance.LOW -> TodoItem.Companion.Importance.LOW
                    TodoItemDbModel.Companion.Importance.BASIC -> TodoItem.Companion.Importance.BASIC
                    TodoItemDbModel.Companion.Importance.IMPORTANT -> TodoItem.Companion.Importance.IMPORTANT
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