package com.bapinaev.domain.repository

import com.bapinaev.domain.model.User

interface UserRepository {

    fun save(user: User)

    fun getUserInfo(login: String): User

    fun removeUserInfo(login: String)
}