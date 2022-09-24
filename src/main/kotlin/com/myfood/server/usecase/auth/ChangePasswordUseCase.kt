package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.ChangePasswordRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository

internal class ChangePasswordUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        userId: String?,
        changePasswordRequest: ChangePasswordRequest
    ): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val (oldPassword, newPassword) = changePasswordRequest
        return when {
            userId.isNullOrBlank() -> {
                response.error = BaseError(message = "User id is null or blank.")
                Resource.Error(response)
            }

            oldPassword.isNullOrBlank() -> {
                response.error = BaseError(message = "Old password is null or blank.")
                Resource.Error(response)
            }

            newPassword.isNullOrBlank() -> {
                response.error = BaseError(message = "New password is null or blank.")
                Resource.Error(response)
            }

            isPasswordInvalid(userId, oldPassword) -> {
                response.error = BaseError(message = "The old password is invalid.")
                Resource.Error(response)
            }

            else -> {
                authRepository.changePassword(userId, newPassword)
            }
        }
    }

    private suspend fun isPasswordInvalid(userId: String, oldPassword: String): Boolean {
        return authRepository.findUserByUserIdAndPassword(userId, oldPassword) == 0L
    }
}