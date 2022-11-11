package com.glebalekseevjk.yasmrhomework.domain.features.synchronize

interface SynchronizedStorage {
    fun getSynchronizedStatus(): Boolean
    fun setSynchronizedStatus(synchronizedStatus: Boolean)
}
