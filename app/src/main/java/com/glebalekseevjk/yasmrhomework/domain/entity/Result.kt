package com.glebalekseevjk.yasmrhomework.domain.entity

enum class ResultStatus {
    SUCCESS,        // Успекх
    LOADING,        // Загрузка
    FAILURE,        // Ошибка
    UNAUTHORIZED    // Выбросить в окно авторизации
}

data class Result<T>(
    val status: ResultStatus,
    val data: T
)