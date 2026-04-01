package com.bapinaev.data.repository

import com.bapinaev.data.room.dao.FlightQueryDao
import com.bapinaev.data.room.mapper.toDomain
import com.bapinaev.data.room.mapper.toEntity
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository
import java.time.Instant

class QueryHistoryRepositoryImpl(
    private val flightQueryDao: FlightQueryDao
) : QueryHistoryRepository {

    override suspend fun save(userLogin: String, query: FlightQuery): FlightQuery {
        val generatedId = flightQueryDao.insertQuery(query.toEntity(userLogin))

        return query.copy(id = generatedId)
    }

    override suspend fun getHistory(userLogin: String): List<FlightQuery> {
        return flightQueryDao.getHistoryByUser(userLogin).map { it.toDomain() }
    }
}