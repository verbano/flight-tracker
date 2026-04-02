package com.bapinaev.flighttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bapinaev.domain.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User = User(
        firstName = "Джон",
        surname = "Самолет",
        age = 25,
        login = "admin",
        password = "1234"
    )
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    fun onLogoutClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.NavigateToLogin) }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.NavigateToMain) }
    }

    fun onEditClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.ShowEditSoon) }
    }

    fun onDeleteClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.ShowDeleteSoon) }
    }
}

sealed interface ProfileEvent {
    data object NavigateToLogin : ProfileEvent
    data object NavigateToMain : ProfileEvent
    data object ShowEditSoon : ProfileEvent
    data object ShowDeleteSoon : ProfileEvent
}
