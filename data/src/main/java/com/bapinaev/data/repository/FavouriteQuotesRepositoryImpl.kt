package com.bapinaev.data.repository

import com.bapinaev.data.room.dao.FavouriteQuoteDao
import com.bapinaev.data.room.mapper.toDomain
import com.bapinaev.data.room.mapper.toEntity
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.repository.FavouriteQuotesRepository

class FavouriteQuotesRepositoryImpl(
    private val dao: FavouriteQuoteDao
) : FavouriteQuotesRepository {

    override suspend fun add(userLogin: String, quote: FavouriteQuote) {
        dao.add(quote.toEntity())
    }

    override suspend fun getAll(userLogin: String): List<FavouriteQuote> {
        return dao.getAll(userLogin)
            .map { it.toDomain() }
    }

    override suspend fun remove(userLogin: String, quote: FavouriteQuote) {
        val id = quote.id
            ?: throw IllegalStateException("Quote must have id")

        dao.remove(id)
    }
}