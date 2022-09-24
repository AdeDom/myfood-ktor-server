package com.myfood.server.usecase.profile

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.profile.ProfileRepository

class ChangeProfileUseCase(
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(
        userId: String?,
        changeProfileRequest: ChangeProfileRequest
    ): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val (name, mobileNo, address) = changeProfileRequest
        return when {
            userId.isNullOrBlank() -> {
                response.error = BaseError(message = "User id is null or blank.")
                Resource.Error(response)
            }

            name.isNullOrBlank() -> {
                response.error = BaseError(message = "Name is null or blank.")
                Resource.Error(response)
            }

            mobileNo.isNullOrBlank() -> {
                response.error = BaseError(message = "Mobile no is null or blank.")
                Resource.Error(response)
            }

            address.isNullOrBlank() -> {
                response.error = BaseError(message = "Address is null or blank.")
                Resource.Error(response)
            }

            else -> {
                profileRepository.changeProfile(userId, changeProfileRequest)
            }
        }
    }
}