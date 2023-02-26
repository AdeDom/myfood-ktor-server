package com.myfood.server.usecase.profile

import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.repositories.profile.ProfileRepository
import com.myfood.server.utility.exception.ApplicationException

internal class ChangeProfileUseCase(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(userId: String?, changeProfileRequest: ChangeProfileRequest): String {
        val (name, mobileNo, address) = changeProfileRequest
        return when {
            userId.isNullOrBlank() -> throw ApplicationException("User id is null or blank.")
            name.isNullOrBlank() -> throw ApplicationException("Name is null or blank.")
            mobileNo.isNullOrBlank() -> throw ApplicationException("Mobile no is null or blank.")
            address.isNullOrBlank() -> throw ApplicationException("Address is null or blank.")
            else -> profileRepository.changeProfile(userId, changeProfileRequest)
        }
    }
}