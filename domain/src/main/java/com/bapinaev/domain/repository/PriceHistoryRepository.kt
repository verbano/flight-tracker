package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PricePoint

interface PriceHistoryRepository {
    fun save(userLogin: String, query: FlightQuery, point: PricePoint)

    fun getHistory(userLogin: String, query: FlightQuery): List<PricePoint>
}