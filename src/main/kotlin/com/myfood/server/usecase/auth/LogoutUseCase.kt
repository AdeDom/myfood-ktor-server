package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(userId: String?): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        return when {
            userId.isNullOrBlank() -> {
                response.error = BaseError(message = "User id is null or blank.")
                Resource.Error(response)
            }

            else -> {
                authRepository.logout(userId)
            }
        }
    }
}