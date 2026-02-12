package com.bapinaev.domain.usecase

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.service.FlightQueryValidator

class SaveQueryToHistoryUseCase(
    private val historyRepository: QueryHistoryRepository,
    private val validator: FlightQueryValidator
) {
    fun execute(query: FlightQuery) {
        validator.execute(query)
        historyRepository.save(query)
    }
}