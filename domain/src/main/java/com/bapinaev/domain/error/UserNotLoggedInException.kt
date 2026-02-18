package com.bapinaev.domain.error

class UserNotLoggedInException(message: String = "User must log in.") : RuntimeException(message)