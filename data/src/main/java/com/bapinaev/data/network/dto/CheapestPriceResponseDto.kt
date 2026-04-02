package com.bapinaev.data.network.dto

import com.google.gson.annotations.SerializedName

data class CheapestPriceResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<PriceDataDto>
)
