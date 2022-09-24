package com.myfood.server.utility.extension

import com.auth0.jwt.JWT
import com.myfood.server.utility.constant.RequestKeyConstant
import com.myfood.server.utility.jwt.JwtHelper
import io.ktor.server.application.*
import io.ktor.server.request.*

val ApplicationCall.userId: String?
    get() = run {
        val authKey = request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val accessToken = authKey?.replace("Bearer", "")?.trim()
        if (!accessToken.isNullOrBlank()) {
            val userId = JWT().decodeJwt(accessToken).getClaim(JwtHelper.USER_ID).asString()
            userId
        } else {
            null
        }
    }