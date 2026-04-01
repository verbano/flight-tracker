package com.bapinaev.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "price_points",
    foreignKeys = [
        ForeignKey(
            entity = FlightQueryEntity::class,
            parentColumns = ["id"],
            childColumns = ["queryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PricePointEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val queryId: Long,
    val userLogin: String,

    val checkedAt: Instant,
    val amount: Long,
    val currency: String
)