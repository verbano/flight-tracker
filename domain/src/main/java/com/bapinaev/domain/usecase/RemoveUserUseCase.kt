package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.UserNotLoggedInException
import com.bapinaev.domain.service.SessionManager
import com.bapinaev.domain.repository.UserRepository

class RemoveUserUseCase (
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend fun execute() {
        val user = sessionManager.getCurrentUser() ?: throw UserNotLoggedInException()

        userRepository.removeUser(user.login)
    }
}
