package com.bapinaev.domain.usecase

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository

class GetQueryHistoryUseCase(
    private val historyRepository: QueryHistoryRepository
) {
    fun execute(): List<FlightQuery> = historyRepository.getHistory()
}