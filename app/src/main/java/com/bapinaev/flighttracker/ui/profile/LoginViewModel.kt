package com.bapinaev.flighttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bapinaev.domain.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val hardcodedUser = User(
        firstName = "Джон",
        surname = "Самолет",
        age = 25,
        login = "admin",
        password = "1234"
    )

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onLoginClicked(login: String, password: String) {
        viewModelScope.launch {
            when {
                login.isBlank() || password.isBlank() -> _events.emit(LoginEvent.EmptyFields)
                login == hardcodedUser.login && password == hardcodedUser.password ->
                    _events.emit(LoginEvent.NavigateToProfile)

                else -> _events.emit(LoginEvent.InvalidCredentials)
            }
        }
    }
}

sealed interface LoginEvent {
    data object EmptyFields : LoginEvent
    data object InvalidCredentials : LoginEvent
    data object NavigateToProfile : LoginEvent
}
