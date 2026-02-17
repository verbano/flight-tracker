package com.bapinaev.domain.usecase

import com.bapinaev.domain.model.UserInfo
import com.bapinaev.domain.repository.UserRepository

class GetUserUseCase (
    private val userRepository: UserRepository
){
    fun execute(login: String): UserInfo = userRepository.getUserInfo(login)
}