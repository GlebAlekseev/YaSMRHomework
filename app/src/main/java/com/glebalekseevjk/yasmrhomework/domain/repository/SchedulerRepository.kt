package com.glebalekseevjk.yasmrhomework.domain.repository

interface SchedulerRepository {
    fun setupPeriodicTimeRefreshTodo()
    fun setupOneTimeCheckSynchronize()
}