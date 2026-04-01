package com.bapinaev.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.Duration
import java.time.LocalDate

@Entity(
    tableName = "favourite_quotes",
    indices = [
        Index(
            value = ["userLogin", "queryId", "checkedAt"],
            unique = true
        )
    ]
)
data class FavouriteQuoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val userLogin: String,

    val queryId: Long,
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val queryCurrency: String,
    val direct: Boolean,

    val amount: Long,
    val currency: String,

    val transfers: Int,
    val checkedAt: Instant,
    val link: String,

    val airline: String,
    val flightNumber: String,
    val originAirport: String,
    val destinationAirport: String,
    val departureAt: Instant,
    val duration: Duration,

    val addedAt: Instant
)