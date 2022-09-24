package com.myfood.server.data.models.base

sealed class ErrorResponse(
    val code: String,
    val message: String,
) {
    object UnauthorizedError : ErrorResponse(code = "401", message = "Unauthorized.")
    object AccessTokenError : ErrorResponse(code = "401-001", message = "Access token expire.")
    object RefreshTokenError : ErrorResponse(code = "401-002", message = "Refresh token expire.")
    object AccessTokenNotAvailableError : ErrorResponse(code = "400-001", message = "Access token not available.")
}