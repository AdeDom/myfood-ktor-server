package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.usecase.category.GetCategoryAllUseCase
import com.myfood.server.usecase.category.InsertCategoryUseCase
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.categoryRoute() {

    val insertCategoryUseCase by inject<InsertCategoryUseCase>()
    post("/api/category/insert") {
        val request = call.receive<InsertCategoryRequest>()
        try {
            val result = insertCategoryUseCase(request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val getCategoryAllUseCase by inject<GetCategoryAllUseCase>()
    get("/api/category/getCategoryAll") {
        try {
            val result = getCategoryAllUseCase()
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