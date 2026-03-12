package com.bapinaev.data.repository

import com.bapinaev.domain.dto.CheapestPriceRequestDto
import com.bapinaev.domain.dto.PriceDataDto
import com.bapinaev.domain.error.PriceNotFoundException
import com.bapinaev.domain.model.FlightInfo
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.repository.CheapestPriceProvider
import com.bapinaev.domain.service.AviasalesApi
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime

class RetrofitCheapestPriceRepository(
    private val api: AviasalesApi,
    private val token: String
) : CheapestPriceProvider {

    override suspend fun getCheapestPrice(query: FlightQuery): PriceQuote {
        val request = query.toDto()

        val response = api.getCheapestPrice(
            token = token,
            origin = request.origin,
            destination = request.destination,
            departureAt = request.departureAt,
            currency = request.currency,
            direct = request.direct
        )

        if (!response.success) {
            throw RuntimeException("Aviasales API returned unsuccessful response")
        }

        val cheapestPrice = response.data.firstOrNull() ?: throw PriceNotFoundException(query)

        return cheapestPrice.toDomain(query)
    }

    private fun PriceDataDto.toDomain(query: FlightQuery): PriceQuote {
        return PriceQuote(
            query = query,

            price = Money(
                amount = price,
                currency = query.currency
            ),

            transfers = transfers,

            checkedAt = Instant.now(),

            flight = FlightInfo(
                airline = airline,
                flightNumber = flightNumber,
                originAirport = originAirport,
                destinationAirport = destinationAirport,
                departureAt = OffsetDateTime.parse(departureAt).toInstant(),
                duration = Duration.ofMinutes(durationTo.toLong())
            ),

            link = "https://www.aviasales.ru/$link"
        )
    }

    private fun FlightQuery.toDto(): CheapestPriceRequestDto {
        return CheapestPriceRequestDto(
            origin = route.origin,
            destination = route.destination,
            departureAt = departureDate.toString(),
            currency = currency.name.lowercase(),
            direct = direct
        )
    }
}
