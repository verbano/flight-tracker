package com.bapinaev.data.repository

import com.bapinaev.data.room.dao.FlightQueryDao
import com.bapinaev.data.room.dao.PricePointDao
import com.bapinaev.data.room.entity.PricePointEntity
import com.bapinaev.data.room.mapper.toDomain
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PricePoint
import com.bapinaev.domain.repository.PriceHistoryRepository

class PriceHistoryRepositoryImpl(
    private val priceDao: PricePointDao
) : PriceHistoryRepository {

    override suspend fun save(
        userLogin: String,
        query: FlightQuery,
        point: PricePoint
    ) {
        val queryId = query.id
            ?: throw IllegalStateException("Query must have id before saving price history")

        val entity = PricePointEntity(
            queryId = queryId,
            userLogin = userLogin,
            checkedAt = point.checkedAt,
            amount = point.price.amount,
            currency = point.price.currency.name
        )

        priceDao.savePricePoint(entity)
    }

    override suspend fun getHistory(
        userLogin: String,
        query: FlightQuery
    ): List<PricePoint> {

        val queryId = query.id ?: return emptyList()

        return priceDao.getHistory(userLogin, queryId).map { it.toDomain() }
    }
}