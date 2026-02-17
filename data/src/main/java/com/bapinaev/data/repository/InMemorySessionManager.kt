package com.bapinaev.data.repository

import com.bapinaev.domain.model.User
import com.bapinaev.domain.repository.SessionManager

class InMemorySessionManager : SessionManager {
    private var currentUser: User? = null

    override fun setCurrentUser(user: User?) {
        currentUser = user
    }

    override fun getCurrentUser(): User? = currentUser

    override fun isUserLoggedIn(): Boolean = currentUser != null
}

