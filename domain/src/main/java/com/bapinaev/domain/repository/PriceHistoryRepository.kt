package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PricePoint

interface PriceHistoryRepository {
    suspend fun save(userLogin: String, query: FlightQuery, point: PricePoint)

    suspend fun getHistory(userLogin: String, query: FlightQuery): List<PricePoint>
}