package com.myfood.server.route.http

import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.profile.ChangeProfileUseCase
import com.myfood.server.usecase.profile.DeleteAccountUseCase
import com.myfood.server.usecase.profile.UserProfileUseCase
import com.myfood.server.utility.extension.deleteAuth
import com.myfood.server.utility.extension.getAuth
import com.myfood.server.utility.extension.putAuth
import com.myfood.server.utility.extension.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.profileRoute() {

    getAuth("/api/profile/user") {
        val userProfileUseCase by closestDI().instance<UserProfileUseCase>()

        val userId = call.userId
        val resource = userProfileUseCase(userId)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    putAuth("/api/profile/changeProfile") {
        val changeProfileUseCase by closestDI().instance<ChangeProfileUseCase>()

        val userId = call.userId
        val request = call.receive<ChangeProfileRequest>()
        val resource = changeProfileUseCase(userId, request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    deleteAuth("/api/profile/deleteAccount") {
        val deleteAccountUseCase by closestDI().instance<DeleteAccountUseCase>()

        val userId = call.userId
        val resource = deleteAccountUseCase(userId)
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