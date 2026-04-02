package com.bapinaev.flighttracker.ui.favorites

import androidx.lifecycle.ViewModel
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.model.FlightInfo
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.model.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        FavoritesUiState(favorites = generateMockFavorites())
    )
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private fun generateMockFavorites(): List<FavouriteQuote> {
        return listOf(
            FavouriteQuote(
                userLogin = "demo",
                addedAt = Instant.now(),
                quote = PriceQuote(
                    query = FlightQuery(
                        route = Route("MOW", "AER"),
                        departureDate = LocalDate.now().plusDays(5)
                    ),
                    price = Money(8900, Currency.RUB),
                    transfers = 0,
                    checkedAt = Instant.now(),
                    flight = FlightInfo(
                        airline = "Aeroflot",
                        flightNumber = "SU123",
                        originAirport = "MOW",
                        destinationAirport = "AER",
                        departureAt = Instant.now(),
                        duration = Duration.ofHours(3)
                    ),
                    link = "https://example.com"
                )
            ),
            FavouriteQuote(
                userLogin = "demo",
                addedAt = Instant.now(),
                quote = PriceQuote(
                    query = FlightQuery(
                        route = Route("LED", "KZN"),
                        departureDate = LocalDate.now().plusDays(10)
                    ),
                    price = Money(4500, Currency.RUB),
                    transfers = 1,
                    checkedAt = Instant.now(),
                    flight = FlightInfo(
                        airline = "S7",
                        flightNumber = "S7101",
                        originAirport = "LED",
                        destinationAirport = "KZN",
                        departureAt = Instant.now(),
                        duration = Duration.ofHours(2).plusMinutes(30)
                    ),
                    link = "https://example.com"
                )
            )
        )
    }
}
