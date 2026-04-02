package com.bapinaev.flighttracker.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.databinding.FragmentSearchBinding
import com.bapinaev.flighttracker.di.AppGraph
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.PriceQuote
import kotlinx.coroutines.launch
import com.google.android.material.chip.Chip
import androidx.core.content.ContextCompat

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            getCheapestPriceUseCase = AppGraph.getCheapestPriceUseCase,
            getQueryHistoryUseCase = AppGraph.getQueryHistoryUseCase,
            addQuoteToFavouritesUseCase = AppGraph.addQuoteToFavouritesUseCase
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        AppGraph.ensureInitialized(requireContext().applicationContext)

        binding.btnFind.setOnClickListener {
            viewModel.search(
                originInput = binding.etOrigin.text?.toString().orEmpty(),
                destinationInput = binding.etDestination.text?.toString().orEmpty(),
                dateInput = binding.etDate.text?.toString().orEmpty(),
                direct = binding.checkboxDirect.isChecked
            )
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistoryDisplay()
        }
        binding.btnAddToFavorites.setOnClickListener {
            viewModel.addCurrentQuoteToFavorites()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect { renderState(it) } }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            SearchEvent.FavoriteAdded -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.search_favorite_added),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is SearchEvent.FavoriteAddFailed -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.search_favorite_error, event.details),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun renderHistory(history: List<FlightQuery>) {
        binding.chipsHistory.removeAllViews()
        val hasHistory = history.isNotEmpty()
        binding.tvHistoryTitle.visibility = if (hasHistory) View.VISIBLE else View.GONE
        binding.btnClearHistory.visibility = if (hasHistory) View.VISIBLE else View.GONE
        binding.chipsHistory.visibility = if (hasHistory) View.VISIBLE else View.GONE

        history.forEach { item ->
            val chip = Chip(requireContext()).apply {
                isClickable = true
                isCheckable = false
                chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.chip_bg)
                setTextColor(ContextCompat.getColor(context, R.color.chip_text))
                chipStrokeWidth = resources.displayMetrics.density
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.stroke_soft)
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

    private fun renderInsights(origin: String, destination: String, transfers: Int) {
        binding.routeTimeline.setTransfers(transfers)
        binding.tvRouteCaption.text = getString(
            R.string.search_insights_route_template,
            origin,
            destination,
            transfers
        )
    }

    private fun renderState(state: SearchUiState) {
        setLoadingState(state.isLoading)
        binding.btnAddToFavorites.visibility =
            if (state.canAddToFavorites) View.VISIBLE else View.GONE
        renderHistory(state.history)
        renderInsights(
            origin = state.insightOrigin,
            destination = state.insightDestination,
            transfers = state.insightTransfers
        )

        binding.tvResult.text = when {
            state.isLoading -> getString(R.string.search_loading)
            state.quote != null -> formatQuote(state.quote)
            state.error == SearchError.EMPTY_FIELDS -> getString(R.string.search_empty_fields_error)
            state.error == SearchError.INVALID_DATE_FORMAT -> getString(R.string.search_invalid_date_format)
            state.error == SearchError.REQUEST_FAILED -> getString(
                R.string.search_request_error,
                state.errorDetails ?: getString(R.string.search_unknown_error)
            )
            else -> binding.tvResult.text
        }
    }
}
