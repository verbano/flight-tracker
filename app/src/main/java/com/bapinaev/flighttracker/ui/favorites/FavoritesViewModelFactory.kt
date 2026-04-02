package com.bapinaev.flighttracker.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bapinaev.domain.usecase.GetFavouriteQuotesUseCase
import com.bapinaev.domain.usecase.RemoveQuoteFromFavouritesUseCase

class FavoritesViewModelFactory(
    private val getFavouriteQuotesUseCase: GetFavouriteQuotesUseCase,
    private val removeQuoteFromFavouritesUseCase: RemoveQuoteFromFavouritesUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(
                getFavouriteQuotesUseCase = getFavouriteQuotesUseCase,
                removeQuoteFromFavouritesUseCase = removeQuoteFromFavouritesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
