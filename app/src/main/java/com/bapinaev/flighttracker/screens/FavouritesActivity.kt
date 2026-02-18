package com.bapinaev.flighttracker.screens

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FavoritesActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val container = findViewById<LinearLayout>(R.id.containerTickets)

        val favourites = listOf(
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

        favourites.forEach { favourite ->
            addFavouriteView(container, favourite)
        }
    }

    private fun addFavouriteView(container: LinearLayout, favourite: FavouriteQuote) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_ticket, container, false)

        val title = view.findViewById<TextView>(R.id.tvTitle)
        val airline = view.findViewById<TextView>(R.id.tvAirline)
        val date = view.findViewById<TextView>(R.id.tvDate)
        val duration = view.findViewById<TextView>(R.id.tvDuration)
        val details = view.findViewById<LinearLayout>(R.id.layoutDetails)

        val quote = favourite.quote
        val flight = quote.flight

        title.text = "${flight.originAirport} → ${flight.destinationAirport}"
        date.text = "Дата: ${quote.query.departureDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
        duration.text = "Цена: ${quote.price.amount} ${quote.price.currency} | ${quote.transfers} пересадка(и)"

        airline.text = "Авиакомпания: ${flight.airline} (${flight.flightNumber})"

        val departureTime = flight.departureAt
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))

        val durationText = TextView(this)
        durationText.text = "Длительность: ${flight.duration.toHours()} ч ${flight.duration.toMinutes() % 60} мин"
        val departureTimeText = TextView(this)
        departureTimeText.text = "Вылет: $departureTime"

        val linkText = TextView(this)
        linkText.text = "Ссылка: ${quote.link}"

        val addedAtText = TextView(this)
        addedAtText.text = "Добавлено в избранное: ${
            favourite.addedAt
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        }"

        val checkedAtText = TextView(this)
        checkedAtText.text = "Проверено: ${
            quote.checkedAt
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        }"

        details.removeAllViews()
        details.addView(departureTimeText)
        details.addView(durationText)
        details.addView(linkText)
        details.addView(addedAtText)
        details.addView(checkedAtText)

        view.setOnClickListener {
            details.visibility = if (details.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        container.addView(view)
    }
}