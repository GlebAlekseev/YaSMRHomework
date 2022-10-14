package com.glebalekseevjk.yasmrhomework.domain.features.synchronized

interface SynchronizedStorage {
    fun getSynchronizedStatus(): Boolean
    fun setSynchronizedStatus(synchronizedStatus: Boolean)
}
