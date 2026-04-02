package com.bapinaev.flighttracker.ui.favorites

import com.bapinaev.domain.model.FavouriteQuote

data class FavoritesUiState(
    val favorites: List<FavouriteQuote> = emptyList()
)
