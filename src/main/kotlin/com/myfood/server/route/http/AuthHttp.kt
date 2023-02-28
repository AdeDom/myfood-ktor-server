package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.ChangePasswordRequest
import com.myfood.server.data.models.request.LoginRequest
import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.request.TokenRequest
import com.myfood.server.usecase.auth.*
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.extension.postAuth
import com.myfood.server.utility.extension.putAuth
import com.myfood.server.utility.extension.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.authRoute() {

    val loginUseCase by inject<LoginUseCase>()
    post("/api/auth/login") {
        val request = call.receive<LoginRequest>()
        try {
            val result = loginUseCase(request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val registerUseCase by inject<RegisterUseCase>()
    post("/api/auth/register") {
        val request = call.receive<RegisterRequest>()
        try {
            val result = registerUseCase(request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val refreshTokenUseCase by inject<RefreshTokenUseCase>()
    post("/api/auth/refreshToken") {
        val request = call.receive<TokenRequest>()
        try {
            val result = refreshTokenUseCase(request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.Forbidden, e.toBaseError())
        }
    }

    val logoutUseCase by inject<LogoutUseCase>()
    postAuth("/api/auth/logout") {
        val userId = call.userId
        try {
            val result = logoutUseCase(userId)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val changePasswordUseCase by inject<ChangePasswordUseCase>()
    putAuth("/api/auth/changePassword") {
        val userId = call.userId
        val request = call.receive<ChangePasswordRequest>()
        try {
            val result = changePasswordUseCase(userId, request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }
}