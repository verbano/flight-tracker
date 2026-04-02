package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.service.SessionManager

class GetQueryHistoryUseCase(
    private val historyRepository: QueryHistoryRepository,
    private val sessionManager: SessionManager
) {
    suspend fun execute(): List<FlightQuery> {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()
        return historyRepository.getHistory(user.login)
    }
}