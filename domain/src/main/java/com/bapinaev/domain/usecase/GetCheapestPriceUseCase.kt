package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.PriceNotFoundException
import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceCheckResult
import com.bapinaev.domain.model.PricePoint
import com.bapinaev.domain.repository.CheapestPriceProvider
import com.bapinaev.domain.repository.PriceHistoryRepository
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.service.FlightQueryValidator

class GetCheapestPriceUseCase(
    private val provider: CheapestPriceProvider,
    private val queryHistoryRepository: QueryHistoryRepository,
    private val priceHistoryRepository : PriceHistoryRepository,
    private val validator: FlightQueryValidator,
    private val sessionManager: SessionManager
) {

    fun execute(query: FlightQuery): PriceCheckResult {
        validator.execute(query)
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        queryHistoryRepository.save(user.login, query)

        val quote = provider.getCheapestPrice(query) ?: throw PriceNotFoundException(query)

        val currentPoint = PricePoint(
            checkedAt = quote.checkedAt,
            price = quote.price
        )

        val history = priceHistoryRepository.getHistory(user.login, query)
        val previousPoint = history.lastOrNull()

        priceHistoryRepository.save(user.login, query, currentPoint)

        val deltaAmount =
            if (previousPoint != null)
                currentPoint.price.amount - previousPoint.price.amount
            else
                null


        return PriceCheckResult(
            quote = quote,
            previousPoint = previousPoint,
            currentPoint = currentPoint,
            deltaAmount = deltaAmount
        )
    }
}