package com.bapinaev.flighttracker.ui.favorites

import androidx.lifecycle.ViewModel
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.usecase.GetFavouriteQuotesUseCase
import com.bapinaev.domain.usecase.RemoveQuoteFromFavouritesUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavouriteQuotesUseCase: GetFavouriteQuotesUseCase,
    private val removeQuoteFromFavouritesUseCase: RemoveQuoteFromFavouritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<FavoritesEvent>()
    val events: SharedFlow<FavoritesEvent> = _events.asSharedFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getFavouriteQuotesUseCase.execute() }
                .onSuccess { favourites ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            favorites = favourites,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }

    fun removeFavorite(favouriteQuote: FavouriteQuote) {
        viewModelScope.launch {
            runCatching { removeQuoteFromFavouritesUseCase.execute(favouriteQuote) }
                .onSuccess {
                    _events.emit(FavoritesEvent.FavoriteRemoved)
                    loadFavorites()
                }
                .onFailure { error ->
                    _events.emit(
                        FavoritesEvent.FavoriteRemoveFailed(
                            details = error.message ?: "Unknown error"
                        )
                    )
                }
        }
    }
}

sealed interface FavoritesEvent {
    data object FavoriteRemoved : FavoritesEvent
    data class FavoriteRemoveFailed(val details: String) : FavoritesEvent
}
