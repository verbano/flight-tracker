package com.bapinaev.flighttracker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Route
import com.bapinaev.domain.usecase.GetCheapestPriceUseCase
import com.bapinaev.domain.usecase.GetQueryHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

class SearchViewModel(
    private val getCheapestPriceUseCase: GetCheapestPriceUseCase,
    private val getQueryHistoryUseCase: GetQueryHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun search(
        originInput: String,
        destinationInput: String,
        dateInput: String,
        direct: Boolean
    ) {
        val origin = originInput.trim().uppercase(Locale.getDefault())
        val destination = destinationInput.trim().uppercase(Locale.getDefault())
        val date = dateInput.trim()

        if (origin.isBlank() || destination.isBlank() || date.isBlank()) {
            _uiState.update {
                it.copy(error = SearchError.EMPTY_FIELDS, errorDetails = null, quote = null)
            }
            return
        }

        val departureDate = runCatching { LocalDate.parse(date) }.getOrNull()
        if (departureDate == null) {
            _uiState.update {
                it.copy(error = SearchError.INVALID_DATE_FORMAT, errorDetails = null, quote = null)
            }
            return
        }

        val query = FlightQuery(
            route = Route(origin = origin, destination = destination),
            departureDate = departureDate,
            currency = Currency.RUB,
            direct = direct
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, errorDetails = null) }
            runCatching { getCheapestPriceUseCase.execute(query) }
                .onSuccess { result ->
                    _uiState.update { state ->
                        val newHistory = state.history.toMutableList().apply {
                            remove(result.quote.query)
                            add(0, result.quote.query)
                            if (size > HISTORY_LIMIT) removeAt(lastIndex)
                        }
                        state.copy(
                            isLoading = false,
                            quote = result.quote,
                            history = newHistory,
                            error = null,
                            errorDetails = null,
                            insightOrigin = result.quote.query.route.origin,
                            insightDestination = result.quote.query.route.destination,
                            insightTransfers = result.quote.transfers
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            quote = null,
                            error = SearchError.REQUEST_FAILED,
                            errorDetails = error.message
                        )
                    }
                }
        }
    }

    fun clearHistoryDisplay() {
        _uiState.update { it.copy(history = emptyList()) }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            runCatching { getQueryHistoryUseCase.execute() }
                .onSuccess { loaded ->
                    _uiState.update { it.copy(history = loaded, error = null, errorDetails = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = SearchError.REQUEST_FAILED, errorDetails = error.message)
                    }
                }
        }
    }

    private companion object {
        private const val HISTORY_LIMIT = 8
    }
}
