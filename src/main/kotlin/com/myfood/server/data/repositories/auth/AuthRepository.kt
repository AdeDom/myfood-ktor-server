package com.myfood.server.data.repositories.auth

import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.response.TokenResponse

internal interface AuthRepository {

    suspend fun login(email: String, password: String): TokenResponse

    suspend fun findUserByEmail(email: String): Long

    suspend fun register(registerRequest: RegisterRequest): TokenResponse

    suspend fun findUserByUserIdAndPassword(userId: String, password: String): Long

    suspend fun changePassword(userId: String, newPassword: String): String

    suspend fun logout(userId: String): String

    suspend fun refreshToken(refreshTokenMaster: String): TokenResponse

    suspend fun updateStatusLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Int

    suspend fun findTokenByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long

    suspend fun findTokenLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long

    suspend fun syncDataAuth(): String

    suspend fun findStatusLoginOrRefreshByAccessToken(accessToken: String): Long
}