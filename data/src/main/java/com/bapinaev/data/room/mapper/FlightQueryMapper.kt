package com.bapinaev.data.room.mapper

import com.bapinaev.data.room.entity.FlightQueryEntity
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Route

fun FlightQueryEntity.toDomain(): FlightQuery {
    return FlightQuery(
        id = id,
        route = Route(origin = origin, destination = destination),
        departureDate = departureDate,
        currency = Currency.valueOf(currency),
        direct = direct
    )
}

fun FlightQuery.toEntity(userLogin: String): FlightQueryEntity {
    return FlightQueryEntity(
        id = 0L,
        userLogin = userLogin,
        origin = route.origin,
        destination = route.destination,
        departureDate = departureDate,
        currency = currency.name,
        direct = direct
    )
}