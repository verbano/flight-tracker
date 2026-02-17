package com.bapinaev.domain.model

data class Money(
    val amount: Long,
    val currency: Currency
) {
    init {
        require(amount >= 0) { "Amount must be >= 0" }
    }
}
