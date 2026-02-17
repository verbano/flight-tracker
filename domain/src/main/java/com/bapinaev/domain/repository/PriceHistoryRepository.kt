package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PricePoint

interface PriceHistoryRepository {
    fun save(userId: Long, query: FlightQuery, point: PricePoint)

    fun getHistory(userId: Long, query: FlightQuery): List<PricePoint>
}