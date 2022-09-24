package com.myfood.server.data.repositories.profile

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.models.response.UserProfileResponse
import com.myfood.server.data.repositories.Resource

interface ProfileRepository {

    suspend fun userProfile(userId: String): Resource<BaseResponse<UserProfileResponse>>

    suspend fun changeProfile(
        userId: String,
        changeProfileRequest: ChangeProfileRequest
    ): Resource<BaseResponse<String>>

    suspend fun deleteAccount(userId: String): Resource<BaseResponse<String>>
}