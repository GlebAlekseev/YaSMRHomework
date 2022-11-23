package com.glebalekseevjk.yasmrhomework.domain.feature

import com.glebalekseevjk.yasmrhomework.domain.entity.Revision

interface RevisionStorage {
    fun getRevision(): Revision?
    fun setRevision(revision: Revision)
    fun clear()
}