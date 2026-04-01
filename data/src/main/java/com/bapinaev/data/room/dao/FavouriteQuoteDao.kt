package com.bapinaev.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bapinaev.data.room.entity.FavouriteQuoteEntity
import java.time.Instant

@Dao
interface FavouriteQuoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(entity: FavouriteQuoteEntity): Long

    @Query("""
        SELECT * FROM favourite_quotes
        WHERE userLogin = :userLogin
        ORDER BY addedAt DESC
    """)
    suspend fun getAll(userLogin: String): List<FavouriteQuoteEntity>

    @Query("DELETE FROM favourite_quotes WHERE id = :id")
    suspend fun remove(id: Long): Int
}