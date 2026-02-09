package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote

interface CheapestPriceProvider {
    fun getCheapestPrice(query: FlightQuery): PriceQuote?
}