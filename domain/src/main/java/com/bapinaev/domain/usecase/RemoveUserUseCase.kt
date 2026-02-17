package com.bapinaev.domain.usecase

import com.bapinaev.domain.repository.UserRepository

class RemoveUserUseCase (
    private val userRepository: UserRepository
){
    fun execute(login: String){
        userRepository.removeUserInfo(login)
    }
}