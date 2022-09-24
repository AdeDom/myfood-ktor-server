package com.myfood.server.route.http

import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.food.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.foodRoute() {

    val myFoodUseCase by inject<MyFoodUseCase>()
    get("/api/my/food") {
        when (val resource = myFoodUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val insertFoodUseCase by inject<InsertFoodUseCase>()
    post("/api/food/insert") {
        val request = call.receive<InsertFoodRequest>()
        when (val resource = insertFoodUseCase(request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val getFoodDetailUseCase by inject<GetFoodDetailUseCase>()
    get("/api/food/detail") {
        val foodId = call.parameters["foodId"]
        when (val resource = getFoodDetailUseCase(foodId)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val getFoodByCategoryIdUseCase by inject<GetFoodByCategoryIdUseCase>()
    get("/api/food/getFoodByCategoryId") {
        val categoryId = call.parameters["categoryId"]
        when (val resource = getFoodByCategoryIdUseCase(categoryId)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val getFoodAndCategoryGroupAllUseCase by inject<GetFoodAndCategoryGroupAllUseCase>()
    get("/api/food/getFoodAndCategoryGroupAll") {
        when (val resource = getFoodAndCategoryGroupAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}