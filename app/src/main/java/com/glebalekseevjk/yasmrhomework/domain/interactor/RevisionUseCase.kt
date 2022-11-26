package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.repository.RevisionRepository

class RevisionUseCase(
    private val revisionRepository: RevisionRepository
) {
    fun getRevision() = revisionRepository.getRevision()

    fun setRevision(revision: Revision) = revisionRepository.setRevision(revision)

    fun clear() = revisionRepository.clear()
}