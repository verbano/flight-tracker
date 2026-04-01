package com.bapinaev.data.room.mapper

import com.bapinaev.data.room.entity.UserEntity
import com.bapinaev.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        firstName = firstName,
        surname = surname,
        age = age,
        login = login,
        password = password
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        firstName = firstName,
        surname = surname,
        age = age,
        login = login,
        password = password
    )
}