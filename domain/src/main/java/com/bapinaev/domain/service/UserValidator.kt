package com.bapinaev.domain.service

import com.bapinaev.domain.error.InvalidUserInfoException
import com.bapinaev.domain.model.User

class UserValidator {

    fun execute(user: User) {
        when {
            user.login.isBlank() -> throw InvalidUserInfoException("User login must not be blank")

            user.firstName.isBlank() ->
                throw InvalidUserInfoException("User firstName must not be blank")

            user.surname.isBlank() ->
                throw InvalidUserInfoException("User surname must not be blank")

            user.password.isBlank() || user.password.length < 5 ->
                throw InvalidUserInfoException(
                    "User password must not be blank and be more than 4 characters"
                )
        }
    }

}
