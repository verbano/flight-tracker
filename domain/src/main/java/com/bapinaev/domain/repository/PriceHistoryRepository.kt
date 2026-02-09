package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PricePoint

interface PriceHistoryRepository {
    fun save(query: FlightQuery, point: PricePoint)

    fun getHistory(query: FlightQuery): List<PricePoint>
}