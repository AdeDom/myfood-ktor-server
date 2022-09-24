package com.myfood.server.usecase.validate

class ValidateEmailUseCase {

    operator fun invoke(email: String?): Boolean {
        return when {
            email.isNullOrBlank() -> false
            email.length < 4 -> false
            else -> true
        }
    }
}