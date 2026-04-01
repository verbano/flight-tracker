package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.repository.FavouriteQuotesRepository
import com.bapinaev.domain.service.SessionManager

class RemoveQuoteFromFavouritesUseCase(
    private val favouritesRepository: FavouriteQuotesRepository,
    private val sessionManager: SessionManager
) {

    suspend fun execute(quote: FavouriteQuote) {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        if (quote.userLogin != user.login) {
            throw IllegalStateException("User cannot remove favourite that does not belong to them")
        }

        favouritesRepository.remove(user.login, quote)
    }
}
