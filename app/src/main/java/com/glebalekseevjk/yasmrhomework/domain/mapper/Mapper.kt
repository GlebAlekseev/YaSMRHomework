package com.glebalekseevjk.yasmrhomework.domain.mapper

interface Mapper<ITEM, DBMODEL> {
    fun mapItemToDbModel(item: ITEM): DBMODEL
    fun mapDbModelToItem(dbModel: DBMODEL): ITEM
}