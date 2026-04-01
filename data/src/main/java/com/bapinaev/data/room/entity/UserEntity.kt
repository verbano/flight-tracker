package com.bapinaev.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey
    val login: String,

    val firstName: String,
    val surname: String,
    val age: Long,
    val password: String
)