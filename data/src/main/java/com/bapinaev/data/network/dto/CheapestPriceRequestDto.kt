package com.bapinaev.data.network.dto

data class CheapestPriceRequestDto(
    val origin: String,
    val destination: String,
    val departureAt: String,
    val currency: String,
    val direct: Boolean
)
