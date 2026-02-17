package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery

interface QueryHistoryRepository {
    fun save(userId: Long, query: FlightQuery)

    fun getHistory(userId: Long): List<FlightQuery>
}