package com.myfood.server.data.repositories.profile

import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.models.response.UserProfileResponse

internal interface ProfileRepository {

    suspend fun userProfile(userId: String): UserProfileResponse

    suspend fun changeProfile(userId: String, changeProfileRequest: ChangeProfileRequest): String

    suspend fun deleteAccount(userId: String): String
}