package com.glebalekseevjk.yasmrhomework.domain.mapper

interface Mapper<ITEM, DB_MODEL> {
    fun mapItemToDbModel(item: ITEM): DB_MODEL
    fun mapDbModelToItem(dbModel: DB_MODEL): ITEM
}