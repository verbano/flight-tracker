package com.bapinaev.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bapinaev.data.room.dao.FavouriteQuoteDao
import com.bapinaev.data.room.dao.FlightQueryDao
import com.bapinaev.data.room.dao.PricePointDao
import com.bapinaev.data.room.dao.UserDao
import com.bapinaev.data.room.entity.FavouriteQuoteEntity
import com.bapinaev.data.room.entity.FlightQueryEntity
import com.bapinaev.data.room.entity.PricePointEntity
import com.bapinaev.data.room.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        FlightQueryEntity::class,
        PricePointEntity::class,
        FavouriteQuoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun flightQueryDao(): FlightQueryDao

    abstract fun pricePointDao(): PricePointDao

    abstract fun favouriteQuoteDao(): FavouriteQuoteDao
}