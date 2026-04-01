package com.bapinaev.data.room.mapper

import com.bapinaev.data.room.entity.FavouriteQuoteEntity
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.model.FlightInfo
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.model.Route

fun FavouriteQuoteEntity.toDomain(): FavouriteQuote {
    return FavouriteQuote(
        userLogin = userLogin,
        addedAt = addedAt,
        quote = PriceQuote(
            query = FlightQuery(
                id = queryId,
                route = Route(origin, destination),
                departureDate = departureDate,
                currency = Currency.valueOf(queryCurrency),
                direct = direct
            ),
            price = Money(amount, Currency.valueOf(currency)),
            transfers = transfers,
            checkedAt = checkedAt,
            link = link,
            flight = FlightInfo(
                airline = airline,
                flightNumber = flightNumber,
                originAirport = originAirport,
                destinationAirport = destinationAirport,
                departureAt = departureAt,
                duration = duration
            )
        )
    )
}

fun FavouriteQuote.toEntity(): FavouriteQuoteEntity {
    val q = quote
    val f = q.flight
    val query = q.query

    return FavouriteQuoteEntity(
        userLogin = userLogin,

        queryId = query.id
            ?: throw IllegalStateException("Query must have id"),

        origin = query.route.origin,
        destination = query.route.destination,
        departureDate = query.departureDate,
        queryCurrency = query.currency.name,
        direct = query.direct,

        amount = q.price.amount,
        currency = q.price.currency.name,

        transfers = q.transfers,
        checkedAt = q.checkedAt,
        link = q.link,

        airline = f.airline,
        flightNumber = f.flightNumber,
        originAirport = f.originAirport,
        destinationAirport = f.destinationAirport,
        departureAt = f.departureAt,
        duration = f.duration,

        addedAt = addedAt
    )
}