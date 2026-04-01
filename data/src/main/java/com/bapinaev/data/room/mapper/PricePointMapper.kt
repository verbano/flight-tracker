package com.bapinaev.data.room.mapper

import com.bapinaev.data.room.entity.PricePointEntity
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PricePoint

fun PricePointEntity.toDomain(): PricePoint {
    return PricePoint(
        checkedAt = checkedAt,
        price = Money(
            amount = amount,
            currency = Currency.valueOf(currency)
        )
    )
}