package com.bapinaev.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bapinaev.data.room.entity.FlightQueryEntity
import java.time.LocalDate

@Dao
interface FlightQueryDao {

    @Insert
    suspend fun insertQuery(query: FlightQueryEntity) : Long

    @Query("SELECT * FROM flight_queries WHERE userLogin = :userLogin ORDER BY createdAt DESC")
    suspend fun getHistoryByUser(userLogin: String): List<FlightQueryEntity>
}