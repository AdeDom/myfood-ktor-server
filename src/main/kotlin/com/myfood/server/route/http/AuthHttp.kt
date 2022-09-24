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
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.authRoute() {

    post("/api/auth/login") {
        val loginUseCase by closestDI().instance<LoginUseCase>()

        val request = call.receive<LoginRequest>()
        val resource = loginUseCase(request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/auth/register") {
        val registerUseCase by closestDI().instance<RegisterUseCase>()

        val request = call.receive<RegisterRequest>()
        val resource = registerUseCase(request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/auth/refreshToken") {
        val refreshTokenUseCase by closestDI().instance<RefreshTokenUseCase>()

        val request = call.receive<TokenRequest>()
        val resource = refreshTokenUseCase(request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.Forbidden, resource.error)
            }
        }
    }

    postAuth("/api/auth/logout") {
        val logoutUseCase by closestDI().instance<LogoutUseCase>()

        val userId = call.userId
        val resource = logoutUseCase(userId)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    putAuth("/api/auth/changePassword") {
        val changePasswordUseCase by closestDI().instance<ChangePasswordUseCase>()

        val userId = call.userId
        val request = call.receive<ChangePasswordRequest>()
        val resource = changePasswordUseCase(userId, request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/auth/syncDataAuth") {
        val syncDataAuthUseCase by closestDI().instance<SyncDataAuthUseCase>()

        val resource = syncDataAuthUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}