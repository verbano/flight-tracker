package com.bapinaev.domain.model

data class User(
    val id: Long,
    val firstName: String,
    val surname: String,
    val age: Long,
    val login: String,
    val password: String
)
