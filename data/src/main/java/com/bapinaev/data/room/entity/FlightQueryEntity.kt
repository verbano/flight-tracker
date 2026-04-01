package com.bapinaev.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.Instant

@Entity(
    tableName = "flight_queries",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["login"],
            childColumns = ["userLogin"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FlightQueryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val userLogin: String,
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val currency: String = "RUB",
    val direct: Boolean = true,
    val createdAt: Instant = Instant.now()
)