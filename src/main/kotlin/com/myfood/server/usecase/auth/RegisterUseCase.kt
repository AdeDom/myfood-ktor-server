package com.myfood.server.usecase.auth

import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.auth.AuthRepository

internal class RegisterUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(registerRequest: RegisterRequest): TokenResponse {
        val (email, password, name) = registerRequest
        return when {
            email.isNullOrBlank() -> throw Throwable("Email is null or blank.")
            password.isNullOrBlank() -> throw Throwable("Password is null or blank.")
            name.isNullOrBlank() -> throw Throwable("Name is null or blank.")
            isValidateEmail(email) -> throw Throwable("This email already exists.")
            else -> authRepository.register(registerRequest)
        }
    }

    private suspend fun isValidateEmail(email: String): Boolean {
        return authRepository.findUserByEmail(email) > 0
    }
}