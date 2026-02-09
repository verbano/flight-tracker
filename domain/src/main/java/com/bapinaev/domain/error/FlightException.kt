package com.bapinaev.domain.error

sealed class FlightException(message: String) : RuntimeException(message)