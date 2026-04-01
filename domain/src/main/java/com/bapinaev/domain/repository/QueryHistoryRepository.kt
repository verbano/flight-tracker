package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery

interface QueryHistoryRepository {
    suspend fun save(userLogin: String, query: FlightQuery) : FlightQuery

    suspend fun getHistory(userLogin: String): List<FlightQuery>
}