package com.myfood.server.data.repositories.auth

import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.resouce.remote.auth.AuthRemoteDataSource
import com.myfood.server.utility.constant.AppConstant
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.jwt.JwtHelper
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

internal class AuthRepositoryImpl(
    private val jwtHelper: JwtHelper,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    override suspend fun login(email: String, password: String): TokenResponse {
        val userId = authRemoteDataSource.findUserIdByEmailAndPassword(
            email,
            encryptSHA(password),
            AppConstant.ACTIVE,
        )
        return if (userId != null) {
            val accessToken = jwtHelper.encodeAccessToken(userId)
            val refreshToken = jwtHelper.encodeRefreshToken(userId)
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        } else {
            throw ApplicationException("Email or password incorrect.")
        }
    }

    override suspend fun findUserByEmail(email: String): Long {
        return authRemoteDataSource.findUserByEmail(email)
    }

    override suspend fun register(registerRequest: RegisterRequest): TokenResponse {
        val insertUserCount = authRemoteDataSource.insertUser(
            userId = UUID.randomUUID().toString().replace("-", ""),
            registerRequest = registerRequest.copy(password = encryptSHA(registerRequest.password!!)),
            AppConstant.ACTIVE,
        ) ?: 0
        return if (insertUserCount > 0) {
            login(registerRequest.email!!, registerRequest.password)
        } else {
            throw ApplicationException("Registration failed")
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

    override suspend fun changePassword(userId: String, newPassword: String): String {
        val isUpdateUserPassword = authRemoteDataSource.updateUserPassword(userId, encryptSHA(newPassword)) == 1
        return if (isUpdateUserPassword) {
            "Change password success."
        } else {
            throw ApplicationException("Change password failed.")
        }
    }

    override suspend fun logout(userId: String): String {
        return "Logout success."
    }

    override suspend fun refreshToken(refreshTokenMaster: String): TokenResponse {
        val userId = jwtHelper.decodeJwtGetUserId(refreshTokenMaster)
        val accessToken = jwtHelper.encodeAccessToken(userId)
        val refreshToken = jwtHelper.encodeRefreshToken(userId)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
}