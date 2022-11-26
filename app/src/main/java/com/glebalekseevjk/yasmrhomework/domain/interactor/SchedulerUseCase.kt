package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.repository.SchedulerRepository

class SchedulerUseCase(
    private val schedulerRepository: SchedulerRepository
) {
    fun setupPeriodicTimeRefreshTodo() = schedulerRepository.setupPeriodicTimeRefreshTodo()

    fun setupOneTimeCheckSynchronize() = schedulerRepository.setupOneTimeCheckSynchronize()
}