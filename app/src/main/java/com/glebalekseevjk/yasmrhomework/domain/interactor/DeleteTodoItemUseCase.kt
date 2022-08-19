package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository

class DeleteTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(todoItem: TodoItem) = todoItemsRepository.deleteTodoItem(todoItem)
}