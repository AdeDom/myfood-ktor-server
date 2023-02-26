package com.myfood.server.utility.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.PublicClaims
import com.myfood.server.plugins.JwtConfiguration
import java.util.*

internal class JwtHelper(
    private val jwtConfiguration: JwtConfiguration,
) {

    companion object {
        const val USER_ID = "user_id"
    }

    fun encodeAccessToken(userId: String): String {
        return JWT.create()
            .withAudience(jwtConfiguration.audience)
            .withIssuer(jwtConfiguration.issuer)
            .withClaim(USER_ID, userId)
            .withExpiresAt(Date(System.currentTimeMillis() + (36_000_00 * 24 * 1)))
            .sign(Algorithm.HMAC512(jwtConfiguration.secret))
    }

    fun encodeRefreshToken(userId: String): String {
        return JWT.create()
            .withAudience(jwtConfiguration.audience)
            .withIssuer(jwtConfiguration.issuer)
            .withClaim(USER_ID, userId)
            .withExpiresAt(Date(System.currentTimeMillis() + (36_000_00 * 24 * 7)))
            .sign(Algorithm.HMAC512(jwtConfiguration.secret))
    }

    fun decodeJwtGetUserId(token: String?): String {
        return JWT().decodeJwt(token).getClaim(USER_ID).asString()
    }

    fun decodeJwtGetExpiresAt(token: String?): Long {
        return JWT().decodeJwt(token).getClaim(PublicClaims.EXPIRES_AT).asLong()
    }
}