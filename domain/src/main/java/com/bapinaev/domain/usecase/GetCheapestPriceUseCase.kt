package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.PriceNotFoundException
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.repository.CheapestPriceProvider
import com.bapinaev.domain.service.FlightQueryValidator

class GetCheapestPriceUseCase(
    private val repository: CheapestPriceProvider,
    private val validator: FlightQueryValidator
) {

    fun execute(query: FlightQuery): PriceQuote {
        validator.execute(query)

        return repository.getCheapestPrice(query)
            ?: throw PriceNotFoundException(query)
    }
}