package com.bapinaev.domain.model

import java.time.Instant

data class PricePoint(
    val checkedAt: Instant,
    val price: Money
)