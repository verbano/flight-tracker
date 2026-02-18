package com.bapinaev.domain.service

import com.bapinaev.domain.model.User

interface SessionManager {
    fun setCurrentUser(user: User?)
    fun getCurrentUser(): User?
    fun isUserLoggedIn(): Boolean
}