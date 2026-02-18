package com.bapinaev.domain.repository

import com.bapinaev.domain.model.User

interface UserRepository {

    fun save(user: User)

    fun getUser(userLogin: String): User

    fun removeUser(userLogin: String)
}