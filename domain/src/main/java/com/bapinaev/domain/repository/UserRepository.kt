package com.bapinaev.domain.repository

import com.bapinaev.domain.model.UserInfo

interface UserRepository {

    fun save(user: UserInfo)

    fun getUserInfo(login: String): UserInfo

    fun removeUserInfo(login: String)
}