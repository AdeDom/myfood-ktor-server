package com.myfood.server.data.resouce.local.auth

import com.myfood.server.data.models.entities.AuthEntity

interface AuthLocalDataSource {

    suspend fun insertAuth(
        authId: String,
        accessToken: String,
        refreshToken: String,
        status: String,
        isBackup: Int,
    ): Int?

    suspend fun getAuthListByStatusLoginOrRefresh(): List<AuthEntity>

    suspend fun updateAuthStatusLogoutByAuthId(authId: String): Int

    suspend fun updateStatusLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Int

    suspend fun findTokenByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long

    suspend fun findTokenLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long

    suspend fun findStatusLoginOrRefreshByAccessToken(accessToken: String): Long

    suspend fun getAuthListByBackupIsLocal(): List<AuthEntity>

    suspend fun updateAuthByBackupIsRemote(): Int

    suspend fun replaceAuthAll(authList: List<AuthEntity>): Int
}