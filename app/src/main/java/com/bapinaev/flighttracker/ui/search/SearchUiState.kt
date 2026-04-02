package com.bapinaev.flighttracker.ui.search

import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote

data class SearchUiState(
    val isLoading: Boolean = false,
    val quote: PriceQuote? = null,
    val history: List<FlightQuery> = emptyList(),
    val error: SearchError? = null,
    val errorDetails: String? = null,
    val insightOrigin: String = "MOW",
    val insightDestination: String = "LED",
    val insightTransfers: Int = 1
)

enum class SearchError {
    EMPTY_FIELDS,
    INVALID_DATE_FORMAT,
    REQUEST_FAILED
}
