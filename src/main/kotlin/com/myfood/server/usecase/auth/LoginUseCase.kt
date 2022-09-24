package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.LoginRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(loginRequest: LoginRequest): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val (email, password) = loginRequest
        return when {
            email.isNullOrBlank() -> {
                response.error = BaseError(message = "Email is null or blank.")
                Resource.Error(response)
            }

            password.isNullOrBlank() -> {
                response.error = BaseError(message = "Password is null or blank.")
                Resource.Error(response)
            }

            else -> {
                authRepository.login(email, password)
            }
        }
    }
}