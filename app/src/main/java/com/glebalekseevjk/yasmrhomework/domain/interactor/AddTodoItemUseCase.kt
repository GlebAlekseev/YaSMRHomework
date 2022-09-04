package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(todoItem: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch{
            todoItemsRepository.addTodoItem(todoItem)
        }
    }
}