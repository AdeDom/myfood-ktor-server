package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.LoginRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.usecase.validate.ValidateEmailUseCase
import com.myfood.server.usecase.validate.ValidatePasswordUseCase

internal class LoginUseCase(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(loginRequest: LoginRequest): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val (email, password) = loginRequest
        return when {
            !validateEmailUseCase(email) -> {
                response.error = BaseError(message = "Email is null or blank.")
                Resource.Error(response)
            }

            !validatePasswordUseCase(password) -> {
                response.error = BaseError(message = "Password is null or blank.")
                Resource.Error(response)
            }

            else -> {
                authRepository.login(email!!, password!!)
            }
        }
    }
}