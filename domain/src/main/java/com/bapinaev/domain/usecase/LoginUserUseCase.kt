package com.bapinaev.domain.usecase

import com.bapinaev.domain.error.InvalidCredentialsException
import com.bapinaev.domain.model.User
import com.bapinaev.domain.repository.SessionManager
import com.bapinaev.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    fun execute(login: String, password: String): User {
        val user = try {
            userRepository.getUser(login)
        } catch (e: Exception) {
            throw InvalidCredentialsException("User with login '$login' not found")
        }
        
        if (user.password != password) {
            throw InvalidCredentialsException("Invalid password")
        }
        
        sessionManager.setCurrentUser(user)
        return user
    }
}

