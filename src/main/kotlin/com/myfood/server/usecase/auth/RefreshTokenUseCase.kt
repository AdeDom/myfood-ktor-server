package com.myfood.server.usecase.auth

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.base.ErrorResponse
import com.myfood.server.data.models.request.TokenRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.Resource
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.utility.jwt.JwtHelper

internal class RefreshTokenUseCase(
    private val jwtHelper: JwtHelper,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(tokenRequest: TokenRequest): Resource<BaseResponse<TokenResponse>> {
        val response = BaseResponse<TokenResponse>()

        val (accessToken, refreshToken) = tokenRequest
        return when {
            accessToken.isNullOrBlank() -> {
                response.error = BaseError(message = "Access token is null or blank.")
                Resource.Error(response)
            }

            refreshToken.isNullOrBlank() -> {
                response.error = BaseError(message = "Refresh token is null or blank.")
                Resource.Error(response)
            }

            isValidateAccessTokenAndRefreshToken(accessToken, refreshToken) -> {
                response.error = BaseError(message = "Access token or refresh token incorrect.")
                Resource.Error(response)
            }

            isValidateIsLogout(accessToken, refreshToken) -> {
                response.error = BaseError(message = "Token is already used.")
                Resource.Error(response)
            }

            isValidateRefreshToken(refreshToken) -> {
                authRepository.updateStatusLogoutByAccessTokenAndRefreshToken(accessToken, refreshToken)
                response.error = BaseError(
                    code = ErrorResponse.RefreshTokenError.code,
                    message = ErrorResponse.RefreshTokenError.message,
                )
                Resource.Error(response)
            }

            else -> {
                authRepository.refreshToken(refreshToken)
            }
        }
    }

    private fun isValidateRefreshToken(refreshToken: String): Boolean {
        val expiresAtClaim = jwtHelper.decodeJwtGetExpiresAt(refreshToken)
        val currentTime = System.currentTimeMillis() / 1_000L
        val isTokenExpire = expiresAtClaim.minus(currentTime) > 0
        return !isTokenExpire
    }

    private suspend fun isValidateAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Boolean {
        val tokenCount = authRepository.findTokenByAccessTokenAndRefreshToken(accessToken, refreshToken)
        return tokenCount == 0L
    }

    private suspend fun isValidateIsLogout(accessToken: String, refreshToken: String): Boolean {
        val tokenLogoutCount = authRepository.findTokenLogoutByAccessTokenAndRefreshToken(accessToken, refreshToken)
        return tokenLogoutCount == 1L
    }
}