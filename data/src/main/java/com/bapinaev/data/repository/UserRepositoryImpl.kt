package com.bapinaev.data.repository

import com.bapinaev.data.room.dao.UserDao
import com.bapinaev.data.room.mapper.*
import com.bapinaev.domain.model.User
import com.bapinaev.domain.repository.UserRepository

class UserRepositoryImpl(
    private val enigmaticUserDao: UserDao
) : UserRepository {

    override suspend fun save(user: User) {
        enigmaticUserDao.saveUser(user.toEntity())
    }

    override suspend fun getUser(userLogin: String): User? {
        val entity = enigmaticUserDao.getUserByLogin(userLogin)
            ?: return null

        return entity.toDomain()
    }

    override suspend fun removeUser(userLogin: String) {
        enigmaticUserDao.removeUserByLogin(userLogin)
    }
}