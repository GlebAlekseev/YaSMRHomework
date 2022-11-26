package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.Revision

interface RevisionRepository {
    fun getRevision(): Revision?
    fun setRevision(revision: Revision)
    fun clear()
}