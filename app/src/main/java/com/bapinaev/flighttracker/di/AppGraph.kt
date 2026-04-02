package com.bapinaev.flighttracker.di

import android.content.Context
import com.bapinaev.data.di.DataModule
import com.bapinaev.data.network.ApiFactory
import com.bapinaev.data.repository.RetrofitCheapestPriceRepository
import com.bapinaev.domain.model.User
import com.bapinaev.domain.usecase.GetCheapestPriceUseCase
import com.bapinaev.domain.usecase.GetQueryHistoryUseCase
import com.bapinaev.domain.service.FlightQueryValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object AppGraph {
    private const val API_BASE_URL = "https://api.travelpayouts.com/aviasales/"
    private const val API_TOKEN = "22264ae54b94ea73742acc78c9c0490f"

    @Volatile
    private var initialized = false

    lateinit var getCheapestPriceUseCase: GetCheapestPriceUseCase
        private set

    lateinit var getQueryHistoryUseCase: GetQueryHistoryUseCase
        private set

    fun ensureInitialized(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return

            val dependencies = DataModule.create(context)
            val api = ApiFactory.createAviasalesApi(API_BASE_URL)
            val cheapestPriceProvider = RetrofitCheapestPriceRepository(api, API_TOKEN)
            val defaultUser = User(
                firstName = "Джон",
                surname = "Самолет",
                age = 25,
                login = "admin",
                password = "1234"
            )

            runBlocking {
                withContext(Dispatchers.IO) {
                    val existingUser = dependencies.userRepository.getUser(defaultUser.login)
                    if (existingUser == null) {
                        dependencies.userRepository.save(defaultUser)
                    }
                }
            }
            dependencies.sessionManager.setCurrentUser(defaultUser)

            getCheapestPriceUseCase = GetCheapestPriceUseCase(
                provider = cheapestPriceProvider,
                queryHistoryRepository = dependencies.queryHistoryRepository,
                priceHistoryRepository = dependencies.priceHistoryRepository,
                validator = FlightQueryValidator(),
                sessionManager = dependencies.sessionManager
            )

            getQueryHistoryUseCase = GetQueryHistoryUseCase(
                historyRepository = dependencies.queryHistoryRepository,
                sessionManager = dependencies.sessionManager
            )

            initialized = true
        }
    }
}
