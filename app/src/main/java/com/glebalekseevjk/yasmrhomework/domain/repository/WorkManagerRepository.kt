package com.glebalekseevjk.yasmrhomework.domain.repository

interface WorkManagerRepository {
    fun setupRefreshTodoWorker()
    fun setupCheckSynchronizedWorker()
}