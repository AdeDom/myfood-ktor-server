package com.myfood.server.usecase.profile

import com.myfood.server.data.models.response.UserProfileResponse
import com.myfood.server.data.repositories.profile.ProfileRepository
import com.myfood.server.utility.exception.ApplicationException

internal class UserProfileUseCase(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(userId: String?): UserProfileResponse {
        return when {
            userId.isNullOrBlank() -> throw ApplicationException("User id is null or blank.")
            else -> profileRepository.userProfile(userId)
        }
    }
}