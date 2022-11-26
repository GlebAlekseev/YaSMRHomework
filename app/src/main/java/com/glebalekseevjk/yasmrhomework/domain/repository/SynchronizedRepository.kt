package com.glebalekseevjk.yasmrhomework.domain.repository

interface SynchronizedRepository {
    fun getSynchronizedStatus(): Boolean
    fun setSynchronizedStatus(synchronizedStatus: Boolean)
}