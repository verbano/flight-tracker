package com.bapinaev.domain.usecase

import com.bapinaev.domain.model.User
import com.bapinaev.domain.repository.UserRepository

class GetUserUseCase (
    private val userRepository: UserRepository
){
    fun execute(login: String): User = userRepository.getUser(login)
}