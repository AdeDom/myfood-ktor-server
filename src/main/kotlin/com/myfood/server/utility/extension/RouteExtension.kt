package com.myfood.server.utility.extension

import com.auth0.jwt.JWT
import com.auth0.jwt.impl.PublicClaims
import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.base.ErrorResponse
import com.myfood.server.utility.constant.RequestKeyConstant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

@KtorDsl
internal fun Route.getAuth(path: String, body: PipelineInterceptor<Unit, ApplicationCall>) {
    get(path) {
        authentication(body)
    }
}

@KtorDsl
internal fun Route.postAuth(path: String, body: PipelineInterceptor<Unit, ApplicationCall>) {
    post(path) {
        authentication(body)
    }
}

@KtorDsl
internal fun Route.putAuth(path: String, body: PipelineInterceptor<Unit, ApplicationCall>) {
    put(path) {
        authentication(body)
    }
}

@KtorDsl
internal fun Route.patchAuth(path: String, body: PipelineInterceptor<Unit, ApplicationCall>) {
    patch(path) {
        authentication(body)
    }
}

@KtorDsl
internal fun Route.deleteAuth(path: String, body: PipelineInterceptor<Unit, ApplicationCall>) {
    delete(path) {
        authentication(body)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.authentication(body: PipelineInterceptor<Unit, ApplicationCall>) {
    val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
    if (authKey != null) {
        val accessToken = authKey.replace("Bearer", "").trim()
        val expiresAtClaim = JWT().decodeJwt(accessToken).getClaim(PublicClaims.EXPIRES_AT).asLong()
        val currentTime = System.currentTimeMillis() / 1_000L
        val isTokenExpire = expiresAtClaim.minus(currentTime) > 0
        if (isTokenExpire) {
            body(Unit)
        } else {
            val response = BaseResponse<Unit>()
            val baseError = BaseError(
                code = ErrorResponse.AccessTokenError.code,
                message = ErrorResponse.AccessTokenError.message
            )
            response.error = baseError
            call.respond(HttpStatusCode.Unauthorized, response)
        }
    } else {
        val response = BaseResponse<Unit>()
        val baseError = BaseError(
            code = ErrorResponse.UnauthorizedError.code,
            message = ErrorResponse.UnauthorizedError.message
        )
        response.error = baseError
        call.respond(HttpStatusCode.Unauthorized, response)
    }
}