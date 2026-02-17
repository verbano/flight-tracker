package com.bapinaev.domain.repository

import com.bapinaev.domain.model.FavoriteQuote
import com.bapinaev.domain.model.PriceQuote

interface FavouriteQuotesRepository {
    fun add(userId: Long, quote: PriceQuote)

    fun getAll(userId: Long) : List<FavoriteQuote>

    fun remove(quote: FavoriteQuote)
}