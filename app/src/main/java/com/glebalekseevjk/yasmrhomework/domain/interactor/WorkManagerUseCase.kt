package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.repository.WorkManagerRepository

class WorkManagerUseCase(
    private val workManagerRepository: WorkManagerRepository
) {
    fun setupRefreshTodoWorker() = workManagerRepository.setupRefreshTodoWorker()
    fun setupCheckSynchronizedWorker() = workManagerRepository.setupCheckSynchronizedWorker()
}