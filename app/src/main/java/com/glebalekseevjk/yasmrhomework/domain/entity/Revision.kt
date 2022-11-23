package com.glebalekseevjk.yasmrhomework.domain.entity

data class Revision(
    val revision: Long = UNDEFINED
){
    companion object{
        const val UNDEFINED = -1L
    }
}