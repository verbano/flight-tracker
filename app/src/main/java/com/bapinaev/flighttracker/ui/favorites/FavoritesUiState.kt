package com.bapinaev.flighttracker.ui.favorites

import com.bapinaev.domain.model.FavouriteQuote

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<FavouriteQuote> = emptyList(),
    val errorMessage: String? = null
)
