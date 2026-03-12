package com.bapinaev.flighttracker.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bapinaev.data.repository.RetrofitCheapestPriceRepository
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.databinding.FragmentSearchBinding
import com.bapinaev.domain.model.Currency
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote
import com.bapinaev.domain.model.Route
import com.bapinaev.domain.service.AviasalesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.chip.Chip
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.Locale

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val cheapestPriceProvider by lazy {
        RetrofitCheapestPriceRepository(
            api = createApi(),
            token = API_TOKEN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.btnFind.setOnClickListener {
            searchCheapestTicket()
        }

        binding.btnClearHistory.setOnClickListener {
            searchHistory.clear()
            renderHistory()
        }

        renderHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchCheapestTicket() {
        val origin = binding.etOrigin.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
        val destination = binding.etDestination.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
        val date = binding.etDate.text?.toString()?.trim().orEmpty()
        val direct = binding.checkboxDirect.isChecked

        if (origin.isBlank() || destination.isBlank() || date.isBlank()) {
            binding.tvResult.text = getString(R.string.search_empty_fields_error)
            return
        }

        val departureDate = runCatching { LocalDate.parse(date) }.getOrNull()
        if (departureDate == null) {
            binding.tvResult.text = getString(R.string.search_invalid_date_format)
            return
        }

        val query = FlightQuery(
            route = Route(origin = origin, destination = destination),
            departureDate = departureDate,
            currency = Currency.RUB,
            direct = direct
        )

        viewLifecycleOwner.lifecycleScope.launch {
            setLoadingState(isLoading = true)
            binding.tvResult.text = getString(R.string.search_loading)

            runCatching {
                withContext(Dispatchers.IO) {
                    cheapestPriceProvider.getCheapestPrice(query)
                }
            }.onSuccess { quote ->
                binding.tvResult.text = formatQuote(quote)
                addHistoryItem(query)
            }.onFailure { error ->
                binding.tvResult.text = getString(
                    R.string.search_request_error,
                    error.message ?: getString(R.string.search_unknown_error)
                )
            }

            setLoadingState(isLoading = false)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.btnFind.isEnabled = !isLoading
        binding.etOrigin.isEnabled = !isLoading
        binding.etDestination.isEnabled = !isLoading
        binding.etDate.isEnabled = !isLoading
        binding.checkboxDirect.isEnabled = !isLoading
    }

    private fun formatQuote(quote: PriceQuote): String {
        val transfersText = if (quote.transfers == 0) {
            getString(R.string.search_transfers_direct)
        } else {
            resources.getQuantityString(R.plurals.search_transfers_count, quote.transfers, quote.transfers)
        }

        return getString(
            R.string.search_api_result_template,
            quote.query.route.origin,
            quote.query.route.destination,
            quote.query.departureDate.toString(),
            quote.price.amount,
            quote.price.currency.name,
            quote.flight.airline,
            quote.flight.flightNumber,
            quote.flight.originAirport,
            quote.flight.destinationAirport,
            transfersText,
            quote.link
        )
    }

    private fun createApi(): AviasalesApi {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AviasalesApi::class.java)
    }

    private fun addHistoryItem(item: FlightQuery) {
        // temp
        searchHistory.remove(item)
        searchHistory.add(0, item)
        if (searchHistory.size > HISTORY_LIMIT) {
            searchHistory.removeAt(searchHistory.lastIndex)
        }
        renderHistory()
    }

    private fun renderHistory() {
        // temp
        binding.chipsHistory.removeAllViews()
        val hasHistory = searchHistory.isNotEmpty()
        binding.tvHistoryTitle.visibility = if (hasHistory) View.VISIBLE else View.GONE
        binding.btnClearHistory.visibility = if (hasHistory) View.VISIBLE else View.GONE
        binding.chipsHistory.visibility = if (hasHistory) View.VISIBLE else View.GONE

        searchHistory.forEach { item ->
            val chip = Chip(requireContext()).apply {
                isClickable = true
                isCheckable = false
                text = getString(
                    R.string.search_history_item_template,
                    item.route.origin,
                    item.route.destination,
                    item.departureDate.toString(),
                    if (item.direct) getString(R.string.search_type_direct) else getString(R.string.search_type_with_stops)
                )
                setOnClickListener {
                    binding.etOrigin.setText(item.route.origin)
                    binding.etDestination.setText(item.route.destination)
                    binding.etDate.setText(item.departureDate.toString())
                    binding.checkboxDirect.isChecked = item.direct
                }
            }
            binding.chipsHistory.addView(chip)
        }
    }

    companion object {
        private const val API_BASE_URL = "https://api.travelpayouts.com/aviasales/"
        private const val API_TOKEN = "22264ae54b94ea73742acc78c9c0490f"
        // temp
        private const val HISTORY_LIMIT = 8
        private val searchHistory = mutableListOf<FlightQuery>()
    }
}
