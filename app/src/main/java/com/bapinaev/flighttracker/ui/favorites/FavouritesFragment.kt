package com.bapinaev.flighttracker.ui.favorites


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.domain.model.FlightInfo
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Money
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.model.Route
import com.bapinaev.flighttracker.R
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerFavorites)

        adapter = FavoritesAdapter(generateMockFavorites())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

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