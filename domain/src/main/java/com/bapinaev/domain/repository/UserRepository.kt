package com.bapinaev.domain.repository

import com.bapinaev.domain.model.User

interface UserRepository {
    fun save(user: User)

    fun getUser(login: String): User

    fun removeUser(userId: Long)
}