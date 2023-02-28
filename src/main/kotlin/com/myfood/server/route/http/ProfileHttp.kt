package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.ChangeProfileRequest
import com.myfood.server.usecase.profile.ChangeProfileUseCase
import com.myfood.server.usecase.profile.DeleteAccountUseCase
import com.myfood.server.usecase.profile.UserProfileUseCase
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import com.myfood.server.utility.extension.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.profileRoute() {

    val userProfileUseCase by inject<UserProfileUseCase>()
    authenticate {
        get("/api/profile/user") {
            try {
                val result = userProfileUseCase(userId)
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

    val changeProfileUseCase by inject<ChangeProfileUseCase>()
    authenticate {
        put("/api/profile/changeProfile") {
            val request = call.receive<ChangeProfileRequest>()
            try {
                val result = changeProfileUseCase(userId, request)
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

    val deleteAccountUseCase by inject<DeleteAccountUseCase>()
    authenticate {
        delete("/api/profile/deleteAccount") {
            try {
                val result = deleteAccountUseCase(userId)
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
}