package com.myfood.server.usecase.auth

import com.myfood.server.data.models.request.TokenRequest
import com.myfood.server.data.models.response.TokenResponse
import com.myfood.server.data.repositories.auth.AuthRepository
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.jwt.JwtHelper

internal class RefreshTokenUseCase(
    private val jwtHelper: JwtHelper,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(tokenRequest: TokenRequest): TokenResponse {
        val (accessToken, refreshToken) = tokenRequest
        return when {
            accessToken.isNullOrBlank() -> {
                throw ApplicationException("Access token is null or blank.")
            }

            refreshToken.isNullOrBlank() -> {
                throw ApplicationException("Refresh token is null or blank.")
            }

            isValidateAccessTokenAndRefreshToken(accessToken, refreshToken) -> {
                throw ApplicationException("Access token or refresh token incorrect.")
            }

            isValidateIsLogout(accessToken, refreshToken) -> {
                throw ApplicationException("Token is already used.")
            }

            isValidateRefreshToken(refreshToken) -> {
                authRepository.updateStatusLogoutByAccessTokenAndRefreshToken(accessToken, refreshToken)
                throw ApplicationException("Token is already used.")
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