package com.myfood.server.usecase.auth

import com.myfood.server.data.models.request.ChangePasswordRequest
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.utility.exception.ApplicationException

internal class ChangePasswordUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        userId: String?,
        changePasswordRequest: ChangePasswordRequest
    ): String {
        val (oldPassword, newPassword) = changePasswordRequest
        return when {
            userId.isNullOrBlank() -> throw ApplicationException("User id is null or blank.")
            oldPassword.isNullOrBlank() -> throw ApplicationException("Old password is null or blank.")
            newPassword.isNullOrBlank() -> throw ApplicationException("New password is null or blank.")
            isPasswordInvalid(userId, oldPassword) -> throw ApplicationException("The old password is invalid.")
            else -> authRepository.changePassword(userId, newPassword)
        }
    }

    private suspend fun isPasswordInvalid(userId: String, oldPassword: String): Boolean {
        return authRepository.findUserByUserIdAndPassword(userId, oldPassword) == 0L
    }
}