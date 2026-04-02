package com.bapinaev.flighttracker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bapinaev.domain.usecase.GetCheapestPriceUseCase
import com.bapinaev.domain.usecase.GetQueryHistoryUseCase

class SearchViewModelFactory(
    private val getCheapestPriceUseCase: GetCheapestPriceUseCase,
    private val getQueryHistoryUseCase: GetQueryHistoryUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(getCheapestPriceUseCase, getQueryHistoryUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}