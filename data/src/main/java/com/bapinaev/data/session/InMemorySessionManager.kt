package com.bapinaev.data.session

import com.bapinaev.domain.model.User
import com.bapinaev.domain.service.SessionManager

class InMemorySessionManager : SessionManager {
    private var currentUser: User? = null

    override fun setCurrentUser(user: User?) {
        currentUser = user
    }

    override fun getCurrentUser(): User? = currentUser

    override fun isUserLoggedIn(): Boolean = currentUser != null
}