package com.bapinaev.flighttracker.ui.favorites


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.di.AppGraph
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: FavoritesAdapter
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(
            getFavouriteQuotesUseCase = AppGraph.getFavouriteQuotesUseCase,
            removeQuoteFromFavouritesUseCase = AppGraph.removeQuoteFromFavouritesUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppGraph.ensureInitialized(requireContext().applicationContext)

        recyclerView = view.findViewById(R.id.recyclerFavorites)
        tvEmpty = view.findViewById(R.id.tvFavoritesEmpty)
        adapter = FavoritesAdapter(emptyList()) { favourite ->
            viewModel.removeFavorite(favourite)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        renderState(state)
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            FavoritesEvent.FavoriteRemoved -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.favorites_removed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is FavoritesEvent.FavoriteRemoveFailed -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.favorites_remove_error, event.details),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderState(state: FavoritesUiState) {
        adapter.submitList(state.favorites)

        val showEmpty = !state.isLoading && state.favorites.isEmpty()
        tvEmpty.visibility = if (showEmpty) View.VISIBLE else View.GONE
        tvEmpty.text = state.errorMessage ?: getString(R.string.favorites_empty)
    }
}
