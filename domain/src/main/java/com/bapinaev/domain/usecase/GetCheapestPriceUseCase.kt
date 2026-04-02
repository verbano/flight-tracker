package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.repository.CheapestPriceProvider
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.service.FlightQueryValidator

class GetCheapestPriceUseCase(
    private val provider: CheapestPriceProvider,
    private val queryHistoryRepository: QueryHistoryRepository,
    private val validator: FlightQueryValidator,
    private val sessionManager: SessionManager
) {

    suspend fun execute(query: FlightQuery): PriceQuote {
        validator.execute(query)
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        val quote = provider.getCheapestPrice(query)
        val persistedQuery = queryHistoryRepository.save(user.login, quote.query)
        return quote.copy(query = persistedQuery)
    }
}