package com.bapinaev.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bapinaev.data.room.entity.PricePointEntity

@Dao
interface PricePointDao {

    @Insert
    suspend fun savePricePoint(point: PricePointEntity)

    @Query("""
        SELECT * FROM price_points 
        WHERE userLogin = :userLogin AND queryId = :queryId
        ORDER BY checkedAt DESC
    """)
    suspend fun getHistory(
        userLogin: String,
        queryId: Long
    ): List<PricePointEntity>
}