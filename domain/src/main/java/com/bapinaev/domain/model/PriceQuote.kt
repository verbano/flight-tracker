package com.bapinaev.domain.model

import java.time.Instant

data class PriceQuote(
    val query: FlightQuery,
    val price: Money,
    val transfers: Int,
    val checkedAt: Instant,
    val flight: FlightInfo,
    val link: String
)
