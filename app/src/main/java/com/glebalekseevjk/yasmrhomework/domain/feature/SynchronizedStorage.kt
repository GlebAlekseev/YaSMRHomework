package com.glebalekseevjk.yasmrhomework.domain.feature

interface SynchronizedStorage {
    fun getSynchronizedStatus(): Boolean
    fun setSynchronizedStatus(synchronizedStatus: Boolean)
}
