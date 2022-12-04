package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.repository.SchedulerRepository
import javax.inject.Inject

class SchedulerUseCase @Inject constructor(
    private val schedulerRepository: SchedulerRepository
) {
    fun setupPeriodicTimeRefreshTodo() = schedulerRepository.setupPeriodicTimeRefreshTodo()

    fun setupOneTimeCheckSynchronize() = schedulerRepository.setupOneTimeCheckSynchronize()
}