package com.bapinaev.domain.usecase

import com.bapinaev.domain.repository.SessionManager

class LogoutUserUseCase(
    private val sessionManager: SessionManager
) {
    fun execute() {
        sessionManager.setCurrentUser(null)
    }
}

