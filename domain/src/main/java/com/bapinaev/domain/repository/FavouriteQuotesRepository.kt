package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FavouriteQuote

interface FavouriteQuotesRepository {
    suspend fun add(userLogin: String, quote: FavouriteQuote)

    suspend fun getAll(userLogin: String) : List<FavouriteQuote>

    suspend fun remove(userLogin: String, quote: FavouriteQuote)
}