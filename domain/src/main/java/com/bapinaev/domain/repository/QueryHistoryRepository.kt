package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery

interface QueryHistoryRepository {
    fun save(userLogin: String, query: FlightQuery)

    fun getHistory(userLogin: String): List<FlightQuery>
}