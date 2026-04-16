package com.bapinaev.flighttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.usecase.GetUserUseCase
import com.bapinaev.domain.usecase.LogoutUserUseCase
import com.bapinaev.domain.usecase.RemoveUserUseCase
import com.bapinaev.domain.usecase.SaveUserUseCase

class ProfileViewModelFactory(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                getUserUseCase = getUserUseCase,
                saveUserUseCase = saveUserUseCase,
                removeUserUseCase = removeUserUseCase,
                logoutUserUseCase = logoutUserUseCase,
                sessionManager = sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
