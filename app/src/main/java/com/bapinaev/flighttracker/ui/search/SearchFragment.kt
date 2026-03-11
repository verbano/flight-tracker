package com.bapinaev.flighttracker.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.databinding.FragmentSearchBinding
import com.bapinaev.domain.model.FlightQuery
import com.bapinaev.domain.model.Route
import com.google.android.material.chip.Chip
import java.time.LocalDate
import java.util.Locale

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.btnFind.setOnClickListener {
            val origin = binding.etOrigin.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
            val dest = binding.etDestination.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
            val date = binding.etDate.text?.toString()?.trim().orEmpty()
            val direct = binding.checkboxDirect.isChecked

            val directText = if (direct) {
                getString(R.string.search_type_direct)
            } else {
                getString(R.string.search_type_with_stops)
            }

            binding.tvResult.text = getString(
                R.string.search_result_template,
                origin,
                dest,
                date,
                directText
            )

            // temp
            if (origin.isNotBlank() && dest.isNotBlank() && date.isNotBlank()) {
                val parsedDate = runCatching { LocalDate.parse(date) }.getOrNull()
                if (parsedDate != null) {
                    addHistoryItem(
                        FlightQuery(
                            route = Route(origin = origin, destination = dest),
                            departureDate = parsedDate,
                            direct = direct
                        )
                    )
                } else {
                    binding.tvResult.text = getString(R.string.search_invalid_date_format)
                }
            }
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
        // temp
        private const val HISTORY_LIMIT = 8
        private val searchHistory = mutableListOf<FlightQuery>()
    }
}
