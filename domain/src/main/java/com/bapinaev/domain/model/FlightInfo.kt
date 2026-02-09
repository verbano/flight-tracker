package com.bapinaev.domain.model

import java.time.Instant

data class FlightInfo(
    val airline: String,
    val flightNumber: String,
    val originAirport: String,
    val destinationAirport: String,
    val departureAt: Instant,
    val durationMinutes: Int
)