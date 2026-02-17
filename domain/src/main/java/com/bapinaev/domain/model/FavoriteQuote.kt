package com.bapinaev.domain.model

import java.time.Instant

data class FavoriteQuote(
    val id: Long,
    val userId: Long,
    val quote: PriceQuote,
    val addedAt: Instant
)
