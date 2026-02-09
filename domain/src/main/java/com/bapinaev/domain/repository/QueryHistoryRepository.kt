package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery

interface QueryHistoryRepository {
    fun save(query: FlightQuery)

    fun getHistory(): List<FlightQuery>
}