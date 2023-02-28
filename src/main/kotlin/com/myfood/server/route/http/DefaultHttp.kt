package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.extension.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Route.defaultRoute() {

    get("/") {
        val messageString = "Welcome to my food."
        call.respond(HttpStatusCode.OK, messageString)
    }

    authenticate {
        get("/api/auth") {
            val response = BaseResponse<String>()
            response.status = ResponseKeyConstant.SUCCESS
            response.result = "Hello auth $userId"
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
