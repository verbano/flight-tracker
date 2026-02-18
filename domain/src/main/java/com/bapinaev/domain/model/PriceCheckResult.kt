package com.bapinaev.domain.model

data class PriceCheckResult(
    val quote: PriceQuote,
    val previousPoint: PricePoint?,
    val currentPoint: PricePoint,
    val deltaAmount: Long?
)