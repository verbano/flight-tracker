package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FavouriteQuote

interface FavouriteQuotesRepository {
    fun add(userLogin: String, quote: FavouriteQuote)

    fun getAll(userLogin: String) : List<FavouriteQuote>

    fun remove(userLogin: String, quote: FavouriteQuote)
}