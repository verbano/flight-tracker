package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.repository.FavouriteQuotesRepository
import com.bapinaev.domain.service.SessionManager

class GetFavouriteQuotesUseCase(
    private val favouritesRepository: FavouriteQuotesRepository,
    private val sessionManager: SessionManager
) {

    suspend fun execute(): List<FavouriteQuote> {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        return favouritesRepository.getAll(user.login)
    }
}
