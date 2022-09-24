package com.myfood.server.data.resouce.remote.profile

import com.myfood.server.data.models.entities.UserEntity
import com.myfood.server.data.models.request.ChangeProfileRequest

internal interface ProfileRemoteDataSource {

    suspend fun getUserByUserId(userId: String): UserEntity?

    suspend fun updateUserProfile(userId: String, changeProfileRequest: ChangeProfileRequest): Int

    suspend fun updateUserStatus(userId: String, status: String): Int
}