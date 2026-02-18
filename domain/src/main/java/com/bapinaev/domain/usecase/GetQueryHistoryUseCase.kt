package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.repository.SessionManager

class GetQueryHistoryUseCase(
    private val historyRepository: QueryHistoryRepository,
    private val sessionManager: SessionManager
) {
    val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

    fun execute(): List<FlightQuery> = historyRepository.getHistory(user.login)
}