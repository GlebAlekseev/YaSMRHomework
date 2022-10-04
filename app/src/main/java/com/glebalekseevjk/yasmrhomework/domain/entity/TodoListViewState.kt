package com.glebalekseevjk.yasmrhomework.domain.entity

data class TodoListViewState(
    val result: Result<List<TodoItem>>,
    val errorMessage: Int
){
    companion object{
        const val OK = -1
        val DEFAULT = TodoListViewState(
            Result(ResultStatus.LOADING, mutableListOf()),
            OK
        )
    }
}
