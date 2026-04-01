package com.bapinaev.domain.model

import java.time.LocalDate

data class FlightQuery(
    val id: Long? = null,
    val route: Route,
    val departureDate: LocalDate,
    val currency: Currency = Currency.RUB,
    val direct: Boolean = true,
)