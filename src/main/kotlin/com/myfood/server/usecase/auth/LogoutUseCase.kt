package com.myfood.server.usecase.auth

import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.utility.exception.ApplicationException

internal class LogoutUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(userId: String?): String {
        return when {
            userId.isNullOrBlank() -> throw ApplicationException("User id is null or blank.")
            else -> authRepository.logout(userId)
        }
    }
}