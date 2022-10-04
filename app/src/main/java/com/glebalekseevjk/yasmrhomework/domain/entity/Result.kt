package com.glebalekseevjk.yasmrhomework.domain.entity

enum class ResultStatus {
    SUCCESS,
    LOADING,
    FAILURE,
    SYN_REQUIRED,
    UNAUTHORIZED
}

data class Result<T>(
    val status: ResultStatus,
    val data: T
)