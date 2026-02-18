package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.service.FlightQueryValidator

class SaveQueryToHistoryUseCase(
    private val historyRepository: QueryHistoryRepository,
    private val validator: FlightQueryValidator,
    private val sessionManager: SessionManager
) {
    fun execute(query: FlightQuery) {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        validator.execute(query)
        historyRepository.save(user.login, query)
    }
}