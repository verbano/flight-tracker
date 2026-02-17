package com.bapinaev.domain.usecase

import com.bapinaev.domain.model.UserInfo
import com.bapinaev.domain.repository.UserRepository
import com.bapinaev.domain.service.UserValidator

class SaveUserUseCase (
    private val userRepository: UserRepository,
    private val validator: UserValidator
){
    fun execute(user: UserInfo) {
        validator.execute(user)

        userRepository.save(user)
    }
}