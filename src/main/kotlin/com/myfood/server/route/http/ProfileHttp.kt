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
import org.koin.ktor.ext.inject

fun Route.profileRoute() {

    val userProfileUseCase by inject<UserProfileUseCase>()
    getAuth("/api/profile/user") {
        val userId = call.userId
        when (val resource = userProfileUseCase(userId)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val changeProfileUseCase by inject<ChangeProfileUseCase>()
    putAuth("/api/profile/changeProfile") {
        val userId = call.userId
        val request = call.receive<ChangeProfileRequest>()
        when (val resource = changeProfileUseCase(userId, request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val deleteAccountUseCase by inject<DeleteAccountUseCase>()
    deleteAuth("/api/profile/deleteAccount") {
        val userId = call.userId
        when (val resource = deleteAccountUseCase(userId)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}