package com.bapinaev.domain.model

data class Money(
    val amount: Int,
    val currency: Currency
) {
    init {
        require(amount >= 0) { "Amount must be >= 0" }
    }
}
