package com.myfood.server.route.http

import com.myfood.server.data.models.request.ChangePasswordRequest
import com.myfood.server.data.models.request.LoginRequest
import com.myfood.server.data.models.request.RegisterRequest
import com.myfood.server.data.models.request.TokenRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.auth.*
import com.myfood.server.utility.extension.postAuth
import com.myfood.server.utility.extension.putAuth
import com.myfood.server.utility.extension.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoute() {

    val loginUseCase by inject<LoginUseCase>()
    post("/api/auth/login") {
        val request = call.receive<LoginRequest>()
        when (val resource = loginUseCase(request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val registerUseCase by inject<RegisterUseCase>()
    post("/api/auth/register") {
        val request = call.receive<RegisterRequest>()
        when (val resource = registerUseCase(request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val refreshTokenUseCase by inject<RefreshTokenUseCase>()
    post("/api/auth/refreshToken") {
        val request = call.receive<TokenRequest>()
        when (val resource = refreshTokenUseCase(request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.Forbidden, resource.error)
            }
        }
    }

    val logoutUseCase by inject<LogoutUseCase>()
    postAuth("/api/auth/logout") {
        val userId = call.userId
        when (val resource = logoutUseCase(userId)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val changePasswordUseCase by inject<ChangePasswordUseCase>()
    putAuth("/api/auth/changePassword") {
        val userId = call.userId
        val request = call.receive<ChangePasswordRequest>()
        when (val resource = changePasswordUseCase(userId, request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val syncDataAuthUseCase by inject<SyncDataAuthUseCase>()
    post("/api/auth/syncDataAuth") {
        when (val resource = syncDataAuthUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}