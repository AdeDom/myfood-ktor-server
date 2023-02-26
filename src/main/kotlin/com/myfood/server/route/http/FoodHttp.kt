package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.usecase.food.*
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.foodRoute() {

    val myFoodUseCase by inject<MyFoodUseCase>()
    get("/api/my/food") {
        try {
            val result = myFoodUseCase()
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val insertFoodUseCase by inject<InsertFoodUseCase>()
    post("/api/food/insert") {
        val request = call.receive<InsertFoodRequest>()
        try {
            val result = insertFoodUseCase(request)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val getFoodDetailUseCase by inject<GetFoodDetailUseCase>()
    get("/api/food/detail") {
        val foodId = call.parameters["foodId"]
        try {
            val result = getFoodDetailUseCase(foodId)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val getFoodByCategoryIdUseCase by inject<GetFoodByCategoryIdUseCase>()
    get("/api/food/getFoodByCategoryId") {
        val categoryId = call.parameters["categoryId"]
        try {
            val result = getFoodByCategoryIdUseCase(categoryId)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val getFoodAndCategoryGroupAllUseCase by inject<GetFoodAndCategoryGroupAllUseCase>()
    get("/api/food/getFoodAndCategoryGroupAll") {
        try {
            val result = getFoodAndCategoryGroupAllUseCase()
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