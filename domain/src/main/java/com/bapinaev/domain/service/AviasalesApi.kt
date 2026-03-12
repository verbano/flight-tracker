package com.bapinaev.domain.service

import com.bapinaev.domain.dto.CheapestPriceResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AviasalesApi {
    @GET("v3/prices_for_dates")
    suspend fun getCheapestPrice(
        @Query("token")
        token: String,

        @Query("origin")
        origin: String,

        @Query("destination")
        destination: String,

        @Query("departure_at")
        departureAt: String,

        @Query("currency")
        currency: String,

        @Query("direct")
        direct: Boolean

    ): CheapestPriceResponseDto
}
