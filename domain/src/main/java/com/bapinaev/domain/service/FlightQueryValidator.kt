package com.bapinaev.domain.service

import com.bapinaev.domain.error.InvalidFlightQueryException
import com.bapinaev.domain.model.FlightQuery

class FlightQueryValidator {
    fun execute(query: FlightQuery) {
        if (query.route.origin.isBlank())
            throw InvalidFlightQueryException("Origin must not be blank")

        if (query.route.destination.isBlank())
            throw InvalidFlightQueryException("Destination must not be blank")

        if (query.route.origin == query.route.destination)
            throw InvalidFlightQueryException("Origin and destination must be different")
    }
}
