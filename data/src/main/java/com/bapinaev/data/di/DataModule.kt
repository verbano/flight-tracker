package com.bapinaev.data.di

import android.content.Context
import androidx.room.Room
import com.bapinaev.data.repository.FavouriteQuotesRepositoryImpl
import com.bapinaev.data.repository.QueryHistoryRepositoryImpl
import com.bapinaev.data.repository.UserRepositoryImpl
import com.bapinaev.data.room.database.AppDatabase
import com.bapinaev.data.session.InMemorySessionManager
import com.bapinaev.domain.repository.FavouriteQuotesRepository
import com.bapinaev.domain.repository.QueryHistoryRepository
import com.bapinaev.domain.repository.UserRepository
import com.bapinaev.domain.service.SessionManager

data class DataDependencies(
    val userRepository: UserRepository,
    val queryHistoryRepository: QueryHistoryRepository,
    val favouriteQuotesRepository: FavouriteQuotesRepository,
    val sessionManager: SessionManager
)

object DataModule {
    private const val DATABASE_NAME = "flight_tracker.db"

    fun create(context: Context): DataDependencies {
        val appContext = context.applicationContext

        val database = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        return DataDependencies(
            userRepository = UserRepositoryImpl(database.userDao()),
            queryHistoryRepository = QueryHistoryRepositoryImpl(database.flightQueryDao()),
            favouriteQuotesRepository = FavouriteQuotesRepositoryImpl(database.favouriteQuoteDao()),
            sessionManager = InMemorySessionManager()
        )
    }
}
