package com.myfood.server.data.repositories.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.entities.AuthMasterEntity
import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.resouce.local.auth.AuthLocalDataSource
import com.myfood.server.data.resouce.remote.auth.AuthRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.jwt.JwtHelper
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

internal class AuthRepositoryImpl(
    private val jwtHelper: JwtHelper,
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val userId = authRemoteDataSource.findUserIdByEmailAndPassword(
            email,
            encryptSHA(password),
            AppConstant.ACTIVE,
        )
        return if (userId != null) {
            val getAuthListOriginal = authLocalDataSource.getAuthListByStatusLoginOrRefresh()
            val getAuthList = getAuthListOriginal.map { authEntity ->
                AuthMasterEntity(
                    authId = authEntity.authId,
                    userId = jwtHelper.decodeJwtGetUserId(authEntity.accessToken),
                    status = authEntity.status,
                    isBackup = authEntity.isBackup,
                    created = authEntity.created,
                    updated = authEntity.updated,
                )
            }
            val getAuthIdList = getAuthList
                .filter { it.userId == userId }
                .map { it.authId }
            var updateAuthLogoutCount = 0
            getAuthIdList.forEach { authId ->
                updateAuthLogoutCount += authLocalDataSource.updateAuthStatusLogoutByAuthId(authId)
            }
            if (updateAuthLogoutCount == getAuthIdList.size) {
                val authId = UUID.randomUUID().toString().replace("-", "")
                val accessToken = jwtHelper.encodeAccessToken(userId)
                val refreshToken = jwtHelper.encodeRefreshToken(userId)
                val status = AppConstant.AUTH_LOGIN
                val isBackup = AppConstant.LOCAL_BACKUP
                val insertAuthCount =
                    authLocalDataSource.insertAuth(authId, accessToken, refreshToken, status, isBackup)
                if (insertAuthCount == 1) {
                    val tokenResponse = TokenResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                    response.status = ResponseKeyConstant.SUCCESS
                    response.result = tokenResponse
                    Resource.Success(response)
                } else {
                    response.error = BaseError(message = "Login invalid.")
                    Resource.Error(response)
                }
            } else {
                response.error = BaseError(message = "Login invalid.")
                Resource.Error(response)
            }
        } else {
            response.error = BaseError(message = "Email or password incorrect.")
            Resource.Error(response)
        }
    }

    override suspend fun findUserByEmail(email: String): Long {
        return authRemoteDataSource.findUserByEmail(email)
    }

    override suspend fun register(registerRequest: RegisterRequest): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val insertUserCount = authRemoteDataSource.insertUser(
            userId = UUID.randomUUID().toString().replace("-", ""),
            registerRequest = registerRequest.copy(password = encryptSHA(registerRequest.password!!)),
            AppConstant.ACTIVE,
        ) ?: 0
        return if (insertUserCount > 0) {
            login(registerRequest.email!!, registerRequest.password)
        } else {
            response.error = BaseError(message = "Registration failed")
            Resource.Error(response)
        }
    }

    private fun encryptSHA(password: String): String {
        var sha = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-512")
            val byteArray = messageDigest.digest(password.toByteArray())
            val bigInteger = BigInteger(1, byteArray)
            sha = bigInteger.toString(16).padStart(64, '0')
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return sha
    }

    override suspend fun findUserByUserIdAndPassword(userId: String, password: String): Long {
        return authRemoteDataSource.findUserByUserIdAndPassword(userId, encryptSHA(password))
    }

    override suspend fun changePassword(userId: String, newPassword: String): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val isUpdateUserPassword = authRemoteDataSource.updateUserPassword(userId, encryptSHA(newPassword)) == 1
        return if (isUpdateUserPassword) {
            response.status = ResponseKeyConstant.SUCCESS
            response.result = "Change password success."
            Resource.Success(response)
        } else {
            response.error = BaseError(message = "Change password failed.")
            Resource.Error(response)
        }
    }

    override suspend fun logout(userId: String): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val getAuthListOriginal = authLocalDataSource.getAuthListByStatusLoginOrRefresh()
        val getAuthList = getAuthListOriginal.map { authEntity ->
            AuthMasterEntity(
                authId = authEntity.authId,
                userId = jwtHelper.decodeJwtGetUserId(authEntity.accessToken),
                status = authEntity.status,
                isBackup = authEntity.isBackup,
                created = authEntity.created,
                updated = authEntity.updated,
            )
        }
        val getAuthIdList = getAuthList
            .filter { it.userId == userId }
            .map { it.authId }
        var updateAuthLogoutCount = 0
        getAuthIdList.forEach { authId ->
            updateAuthLogoutCount += authLocalDataSource.updateAuthStatusLogoutByAuthId(authId)
        }
        return if (updateAuthLogoutCount == getAuthIdList.size) {
            response.status = ResponseKeyConstant.SUCCESS
            response.result = "Logout success."
            return Resource.Success(response)
        } else {
            response.error = BaseError(message = "Logout failed.")
            Resource.Error(response)
        }
    }

    override suspend fun refreshToken(refreshTokenMaster: String): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val userId = jwtHelper.decodeJwtGetUserId(refreshTokenMaster)

        val getAuthListOriginal = authLocalDataSource.getAuthListByStatusLoginOrRefresh()
        val getAuthList = getAuthListOriginal.map { authEntity ->
            AuthMasterEntity(
                authId = authEntity.authId,
                userId = jwtHelper.decodeJwtGetUserId(authEntity.accessToken),
                status = authEntity.status,
                isBackup = authEntity.isBackup,
                created = authEntity.created,
                updated = authEntity.updated,
            )
        }
        val getAuthIdList = getAuthList
            .filter { it.userId == userId }
            .map { it.authId }
        var updateAuthLogoutCount = 0
        getAuthIdList.forEach { authId ->
            updateAuthLogoutCount += authLocalDataSource.updateAuthStatusLogoutByAuthId(authId)
        }
        return if (updateAuthLogoutCount == getAuthIdList.size) {
            val authId = UUID.randomUUID().toString().replace("-", "")
            val accessToken = jwtHelper.encodeAccessToken(userId)
            val refreshToken = jwtHelper.encodeRefreshToken(userId)
            val status = AppConstant.AUTH_REFRESH
            val isBackup = AppConstant.LOCAL_BACKUP
            val insertAuthCount = authLocalDataSource.insertAuth(authId, accessToken, refreshToken, status, isBackup)
            if (insertAuthCount == 1) {
                val tokenResponse = TokenResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )
                response.status = ResponseKeyConstant.SUCCESS
                response.result = tokenResponse
                Resource.Success(response)
            } else {
                response.error = BaseError(message = "Refresh token invalid.")
                Resource.Error(response)
            }
        } else {
            response.error = BaseError(message = "Refresh token invalid.")
            Resource.Error(response)
        }
    }

    override suspend fun updateStatusLogoutByAccessTokenAndRefreshToken(
        accessToken: String,
        refreshToken: String,
    ): Int {
        return authLocalDataSource.updateStatusLogoutByAccessTokenAndRefreshToken(accessToken, refreshToken)
    }

    override suspend fun findTokenByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long {
        return authLocalDataSource.findTokenByAccessTokenAndRefreshToken(accessToken, refreshToken)
    }

    override suspend fun findTokenLogoutByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Long {
        return authLocalDataSource.findTokenLogoutByAccessTokenAndRefreshToken(accessToken, refreshToken)
    }

    override suspend fun syncDataAuth(): Resource<BaseResponse<String>> {
        val response = BaseResponse<String>()

        val authLocalList = authLocalDataSource.getAuthListByBackupIsLocal()
        val replaceAuthRemoteCount = authRemoteDataSource.replaceAuthAll(authLocalList)
        return if (authLocalList.size == replaceAuthRemoteCount) {
            val updateAuthBackupCount = authLocalDataSource.updateAuthByBackupIsRemote()
            if (authLocalList.size == updateAuthBackupCount) {
                val authRemoteList = authRemoteDataSource.getAuthAll()
                val replaceAuthLocalCount = authLocalDataSource.replaceAuthAll(authRemoteList)
                if (authRemoteList.size == replaceAuthLocalCount) {
                    response.result = "Sync data success."
                    response.status = ResponseKeyConstant.SUCCESS
                    Resource.Success(response)
                } else {
                    response.error = BaseError(message = "Sync data failed (3).")
                    Resource.Error(response)
                }
            } else {
                response.error = BaseError(message = "Sync data failed (2).")
                Resource.Error(response)
            }
        } else {
            response.error = BaseError(message = "Sync data failed (1).")
            Resource.Error(response)
        }
    }

    override suspend fun findStatusLoginOrRefreshByAccessToken(accessToken: String): Long {
        return authLocalDataSource.findStatusLoginOrRefreshByAccessToken(accessToken)
    }
}