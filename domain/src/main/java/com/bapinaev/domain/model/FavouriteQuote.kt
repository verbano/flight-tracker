package com.bapinaev.domain.model

import java.time.Instant

data class FavouriteQuote(
    val userLogin: String,
    val quote: PriceQuote,
    val addedAt: Instant
)
