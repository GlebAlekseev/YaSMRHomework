package com.glebalekseevjk.yasmrhomework.domain.features.revision

import com.glebalekseevjk.yasmrhomework.domain.entity.Revision

interface RevisionStorage {
    fun getRevision(): Revision?
    fun setRevision(revision: Revision)
    fun clear()
}