package com.bapinaev.flighttracker.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bapinaev.domain.model.FavouriteQuote
import com.bapinaev.flighttracker.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FavoritesAdapter(
    private val items: List<FavouriteQuote>
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val airline: TextView = itemView.findViewById(R.id.tvAirline)
        private val date: TextView = itemView.findViewById(R.id.tvDate)
        private val duration: TextView = itemView.findViewById(R.id.tvDuration)
        private val details: LinearLayout = itemView.findViewById(R.id.layoutDetails)

        private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun bind(favourite: FavouriteQuote) {

            val quote = favourite.quote
            val flight = quote.flight

            title.text = "${flight.originAirport} → ${flight.destinationAirport}"

            airline.text =
                "Авиакомпания: ${flight.airline} (${flight.flightNumber})"

            date.text =
                "Дата: ${quote.query.departureDate.format(dateFormatter)}"

            duration.text =
                "Цена: ${quote.price.amount} ${quote.price.currency} | ${quote.transfers} пересадка(и)"

            bindDetails(favourite)

            itemView.setOnClickListener {
                details.visibility =
                    if (details.visibility == View.VISIBLE)
                        View.GONE
                    else
                        View.VISIBLE
            }
        }

        private fun bindDetails(favourite: FavouriteQuote) {

            details.removeAllViews()

            val quote = favourite.quote
            val flight = quote.flight

            val context = itemView.context

            val departureTime = flight.departureAt
                .atZone(ZoneId.systemDefault())
                .format(timeFormatter)

            val departureText = TextView(context).apply {
                text = "Вылет: $departureTime"
            }

            val durationText = TextView(context).apply {
                text = "Длительность: ${flight.duration.toHours()} ч ${flight.duration.toMinutes() % 60} мин"
            }

            val linkText = TextView(context).apply {
                text = "Ссылка: ${quote.link}"
            }

            val addedText = TextView(context).apply {
                text = "Добавлено: ${
                    favourite.addedAt
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                }"
            }

            val checkedText = TextView(context).apply {
                text = "Проверено: ${
                    quote.checkedAt
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                }"
            }

            details.addView(departureText)
            details.addView(durationText)
            details.addView(linkText)
            details.addView(addedText)
            details.addView(checkedText)
        }
    }
}