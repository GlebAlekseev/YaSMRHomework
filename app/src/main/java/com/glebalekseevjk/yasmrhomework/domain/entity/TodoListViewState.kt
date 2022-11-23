package com.glebalekseevjk.yasmrhomework.domain.entity

data class TodoListViewState(
    val result: Result<List<TodoItem>>,
    val errorMessage: String
) {
    companion object {
        const val OK = ""
        val PLUG = TodoListViewState(
            Result(ResultStatus.LOADING, mutableListOf()),
            OK
        )
    }
}
