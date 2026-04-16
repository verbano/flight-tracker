package com.bapinaev.flighttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bapinaev.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onLoginClicked(login: String, password: String) {
        viewModelScope.launch {
            when {
                login.isBlank() || password.isBlank() -> _events.emit(LoginEvent.EmptyFields)
                else -> runCatching {
                    loginUserUseCase.execute(
                        login = login.trim(),
                        password = password
                    )
                }
                    .onSuccess { _events.emit(LoginEvent.NavigateToProfile) }
                    .onFailure { _events.emit(LoginEvent.InvalidCredentials) }
            }
        }
    }
}

sealed interface LoginEvent {
    data object EmptyFields : LoginEvent
    data object InvalidCredentials : LoginEvent
    data object NavigateToProfile : LoginEvent
}
