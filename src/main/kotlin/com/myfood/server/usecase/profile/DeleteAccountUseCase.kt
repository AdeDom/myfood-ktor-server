package com.myfood.server.usecase.profile

import com.myfood.server.data.repositories.profile.ProfileRepository
import com.myfood.server.utility.exception.ApplicationException

internal class DeleteAccountUseCase(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(userId: String?): String {
        return when {
            userId.isNullOrBlank() -> throw ApplicationException("User id is null or blank.")
            else -> profileRepository.deleteAccount(userId)
        }
    }
}