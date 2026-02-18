package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.repository.FavouriteQuotesRepository
import com.bapinaev.domain.service.SessionManager
import java.time.Instant

class AddQuoteToFavouritesUseCase(
    private val favouritesRepository: FavouriteQuotesRepository,
    private val sessionManager: SessionManager
) {

    fun execute(quote: PriceQuote) {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        val favouriteQuote = FavouriteQuote(
            userLogin = user.login,
            quote = quote,
            addedAt = Instant.now()
        )

        favouritesRepository.add(user.login, favouriteQuote)
    }
}
