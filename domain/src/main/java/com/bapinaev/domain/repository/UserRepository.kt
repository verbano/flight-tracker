package com.bapinaev.domain.repository

import com.bapinaev.domain.model.User

interface UserRepository {

    suspend fun save(user: User)

    suspend fun getUser(userLogin: String): User?

    suspend fun removeUser(userLogin: String)
}