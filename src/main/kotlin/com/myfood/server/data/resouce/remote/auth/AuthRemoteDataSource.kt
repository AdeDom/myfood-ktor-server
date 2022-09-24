package com.myfood.server.data.resouce.remote.auth

import com.myfood.server.data.models.entities.AuthEntity
import com.myfood.server.data.models.request.RegisterRequest

interface AuthRemoteDataSource {

    suspend fun findUserIdByEmailAndPassword(email: String, password: String, status: String): String?

    suspend fun findUserByEmail(email: String): Long

    suspend fun insertUser(userId: String, registerRequest: RegisterRequest, status: String): Int?

    suspend fun findUserByUserIdAndPassword(userId: String, password: String): Long

    suspend fun updateUserPassword(userId: String, password: String): Int

    suspend fun replaceAuthAll(authList: List<AuthEntity>): Int

    suspend fun getAuthAll(): List<AuthEntity>
}