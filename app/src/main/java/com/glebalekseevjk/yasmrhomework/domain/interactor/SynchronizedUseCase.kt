package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.repository.SynchronizedRepository
import javax.inject.Inject

class SynchronizedUseCase @Inject constructor(
    private val synchronizedRepository: SynchronizedRepository
) {
    fun setSynchronizedStatus(synchronizedStatus: Boolean) =
        synchronizedRepository.setSynchronizedStatus(synchronizedStatus)

    fun getSynchronizedStatus() = synchronizedRepository.getSynchronizedStatus()
}