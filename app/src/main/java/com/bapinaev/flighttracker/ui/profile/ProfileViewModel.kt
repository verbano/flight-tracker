package com.bapinaev.flighttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bapinaev.domain.model.User
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.usecase.GetUserUseCase
import com.bapinaev.domain.usecase.LogoutUserUseCase
import com.bapinaev.domain.usecase.RemoveUserUseCase
import com.bapinaev.domain.usecase.SaveUserUseCase
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

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    init {
        loadUser()
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUserUseCase.execute()
            _events.emit(ProfileEvent.NavigateToLogin)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.NavigateToMain) }
    }

    fun onEditClicked() {
        viewModelScope.launch {
            _events.emit(ProfileEvent.RequestEditProfile(_uiState.value.user))
        }
    }

    fun onProfileEditSubmitted(firstName: String, surname: String, ageInput: String, password: String) {
        viewModelScope.launch {
            val trimmedFirstName = firstName.trim()
            val trimmedSurname = surname.trim()
            val trimmedPassword = password.trim()
            val parsedAge = ageInput.trim().toLongOrNull()

            if (trimmedFirstName.isBlank() || trimmedSurname.isBlank() || trimmedPassword.isBlank()) {
                _events.emit(ProfileEvent.OperationFailed("Заполните все поля"))
                return@launch
            }

            if (parsedAge == null || parsedAge <= 0L) {
                _events.emit(ProfileEvent.OperationFailed("Возраст должен быть положительным числом"))
                return@launch
            }

            val updated = _uiState.value.user.copy(
                firstName = trimmedFirstName,
                surname = trimmedSurname,
                age = parsedAge,
                password = trimmedPassword
            )

            runCatching {
                saveUserUseCase.execute(updated)
                sessionManager.setCurrentUser(updated)
                updated
            }
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(user = user)
                    _events.emit(ProfileEvent.ProfileUpdated)
                }
                .onFailure { error ->
                    _events.emit(ProfileEvent.OperationFailed(error.message ?: "Не удалось обновить профиль"))
                }
        }
    }

    fun onDeleteClicked() {
        viewModelScope.launch { _events.emit(ProfileEvent.ConfirmDeleteAccount) }
    }

    fun onDeleteConfirmed() {
        viewModelScope.launch {
            runCatching {
                removeUserUseCase.execute()
                logoutUserUseCase.execute()
            }
                .onSuccess { _events.emit(ProfileEvent.NavigateToLogin) }
                .onFailure { error ->
                    _events.emit(ProfileEvent.OperationFailed(error.message ?: "Не удалось удалить аккаунт"))
                }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val currentUser = sessionManager.getCurrentUser()
            if (currentUser == null) {
                _events.emit(ProfileEvent.NavigateToLogin)
                return@launch
            }

            runCatching { getUserUseCase.execute(currentUser.login) }
                .onSuccess { fromDb ->
                    if (fromDb == null) {
                        logoutUserUseCase.execute()
                        _events.emit(ProfileEvent.NavigateToLogin)
                    } else {
                        sessionManager.setCurrentUser(fromDb)
                        _uiState.value = _uiState.value.copy(user = fromDb)
                    }
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(user = currentUser)
                }
        }
    }
}

sealed interface ProfileEvent {
    data object NavigateToLogin : ProfileEvent
    data object NavigateToMain : ProfileEvent
    data class RequestEditProfile(val user: User) : ProfileEvent
    data object ConfirmDeleteAccount : ProfileEvent
    data object ProfileUpdated : ProfileEvent
    data class OperationFailed(val message: String) : ProfileEvent
}
