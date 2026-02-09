package com.bapinaev.data.repository

import com.bapinaev.domain.model.FlightInfo
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.repository.CheapestPriceProvider
import java.time.Instant
import java.time.ZoneOffset

class FakeCheapestPriceRepository : CheapestPriceProvider {
    override fun getCheapestPrice(query: FlightQuery): PriceQuote {
        return PriceQuote(
            query = query,
            price = Money(10500, query.currency),
            transfers = 0,
            checkedAt = Instant.now(),
            flight = FlightInfo(
                airline = "DP",
                flightNumber = "202",
                originAirport = "LED",
                destinationAirport = "VKO",
                departureAt = query.departureDate.atStartOfDay().toInstant(ZoneOffset.UTC),
                durationMinutes = 90
            ),
            link = "https://example.com"
        )
    }
}
