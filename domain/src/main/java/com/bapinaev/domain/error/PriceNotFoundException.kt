package com.bapinaev.domain.error

import com.bapinaev.domain.model.FlightQuery

class PriceNotFoundException(query: FlightQuery) :
    FlightException("No price found for query: $query")
