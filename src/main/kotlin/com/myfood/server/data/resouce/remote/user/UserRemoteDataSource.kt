package com.myfood.server.data.resouce.remote.user

import com.myfood.server.data.models.entities.UserEntity

internal interface UserRemoteDataSource {

    suspend fun getUserAll(): List<UserEntity>
}