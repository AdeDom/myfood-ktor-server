package com.myfood.server.usecase.auth

import com.myfood.server.data.models.request.LoginRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.usecase.validate.ValidateEmailUseCase
import com.myfood.server.usecase.validate.ValidatePasswordUseCase
import com.myfood.server.utility.exception.ApplicationException

internal class LoginUseCase(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(loginRequest: LoginRequest): TokenResponse {
        val (email, password) = loginRequest
        return when {
            !validateEmailUseCase(email) -> throw ApplicationException("Email is null or blank.")
            !validatePasswordUseCase(password) -> throw ApplicationException("Password is null or blank.")
            else -> authRepository.login(email!!, password!!)
        }
    }
}