package com.bapinaev.data.network.dto

import com.google.gson.annotations.SerializedName

data class PriceDataDto(
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("origin_airport")
    val originAirport: String,
    @SerializedName("destination_airport")
    val destinationAirport: String,
    @SerializedName("price")
    val price: Long,
    @SerializedName("airline")
    val airline: String,
    @SerializedName("flight_number")
    val flightNumber: String,
    @SerializedName("departure_at")
    val departureAt: String,
    @SerializedName("return_at")
    val returnAt: String?,
    @SerializedName("transfers")
    val transfers: Int,
    @SerializedName("return_transfers")
    val returnTransfers: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("duration_to")
    val durationTo: Int,
    @SerializedName("duration_back")
    val durationBack: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("currency")
    val currency: String
)
