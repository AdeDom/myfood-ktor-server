package com.myfood.server.usecase.profile

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.profile.ProfileRepository

internal class DeleteAccountUseCase(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(userId: String?): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        return when {
            userId.isNullOrBlank() -> {
                response.error = BaseError(message = "User id is null or blank.")
                Resource.Error(response)
            }

            else -> {
                profileRepository.deleteAccount(userId)
            }
        }
    }
}