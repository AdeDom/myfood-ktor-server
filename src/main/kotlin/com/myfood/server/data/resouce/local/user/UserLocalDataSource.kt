package com.myfood.server.data.resouce.local.user

import com.myfood.server.data.models.entities.UserEntity

internal interface UserLocalDataSource {

    suspend fun getUserByUserId(userId: String): UserEntity?

    suspend fun getUserAll(): List<UserEntity>

    suspend fun insertUserAll(userList: List<UserEntity>): Int

    suspend fun deleteUserAll()
}