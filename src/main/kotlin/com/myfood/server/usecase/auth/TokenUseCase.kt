package com.myfood.server.usecase.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.impl.PublicClaims
import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.ErrorResponse
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.jwt.JwtHelper

internal class TokenUseCase(
    private val authRepository: AuthRepository,
) {

    suspend fun isValidateToken(authKey: String?): Boolean {
        return if (!authKey.isNullOrBlank()) {
            val accessToken = authKey.replace("Bearer", "").trim()
            val expiresAtClaim = JWT().decodeJwt(accessToken).getClaim(PublicClaims.EXPIRES_AT).asLong()
            val currentTime = System.currentTimeMillis() / 1_000L
            val isTokenExpire = expiresAtClaim.minus(currentTime) > 0
            if (isTokenExpire) {
                val statusLoginOrRefreshCount = authRepository.findStatusLoginOrRefreshByAccessToken(accessToken)
                statusLoginOrRefreshCount != 1L
            } else {
                true
            }
        } else {
            true
        }
    }

    suspend fun getBaseError(authKey: String?): BaseError? {
        return if (authKey != null) {
            val accessToken = authKey.replace("Bearer", "").trim()
            val expiresAtClaim = JWT().decodeJwt(accessToken).getClaim(PublicClaims.EXPIRES_AT).asLong()
            val currentTime = System.currentTimeMillis() / 1_000L
            val isTokenExpire = expiresAtClaim.minus(currentTime) > 0
            if (isTokenExpire) {
                val statusLoginOrRefreshCount = authRepository.findStatusLoginOrRefreshByAccessToken(accessToken)
                if (statusLoginOrRefreshCount == 1L) {
                    null
                } else {
                    BaseError(
                        code = ErrorResponse.AccessTokenNotAvailableError.code,
                        message = ErrorResponse.AccessTokenNotAvailableError.message
                    )
                }
            } else {
                BaseError(
                    code = ErrorResponse.AccessTokenError.code,
                    message = ErrorResponse.AccessTokenError.message
                )
            }
        } else {
            BaseError(
                code = ErrorResponse.UnauthorizedError.code,
                message = ErrorResponse.UnauthorizedError.message
            )
        }
    }

    suspend fun getBaseError2(authKey: String?): String {
        return if (authKey != null) {
            val accessToken = authKey.replace("Bearer", "").trim()
            val expiresAtClaim = JWT().decodeJwt(accessToken).getClaim(PublicClaims.EXPIRES_AT).asLong()
            val currentTime = System.currentTimeMillis() / 1_000L
            val isTokenExpire = expiresAtClaim.minus(currentTime) > 0
            if (isTokenExpire) {
                val statusLoginOrRefreshCount = authRepository.findStatusLoginOrRefreshByAccessToken(accessToken)
                if (statusLoginOrRefreshCount == 1L) {
                    ResponseKeyConstant.SUCCESS
                } else {
                    ErrorResponse.AccessTokenNotAvailableError.message
                }
            } else {
                ErrorResponse.AccessTokenError.message
            }
        } else {
            ErrorResponse.UnauthorizedError.message
        }
    }

    fun getUserId(authKey: String?): String {
        val accessToken = authKey?.replace("Bearer", "")?.trim()
        return JWT().decodeJwt(accessToken).getClaim(JwtHelper.USER_ID).asString()
    }
}