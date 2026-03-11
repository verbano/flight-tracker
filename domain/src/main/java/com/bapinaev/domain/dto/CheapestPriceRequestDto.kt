package com.bapinaev.domain.dto

import com.google.gson.annotations.SerializedName

data class CheapestPriceRequestDto(
    @SerializedName("origin")
    val origin: String,

    @SerializedName("destination")
    val destination: String,

    @SerializedName("departure_at")
    val departureAt: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("direct")
    val direct: Boolean
)