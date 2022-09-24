package com.myfood.server.route.http

import com.myfood.server.data.models.request.InsertFoodRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.food.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.foodRoute() {

    get("/api/my/food") {
        val myFoodUseCase by closestDI().instance<MyFoodUseCase>()

        val resource = myFoodUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/food/insert") {
        val insertFoodUseCase by closestDI().instance<InsertFoodUseCase>()

        val request = call.receive<InsertFoodRequest>()
        val resource = insertFoodUseCase(request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    get("/api/food/detail") {
        val getFoodDetailUseCase by closestDI().instance<GetFoodDetailUseCase>()

        val foodId = call.parameters["foodId"]
        val resource = getFoodDetailUseCase(foodId)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    get("/api/food/getFoodByCategoryId") {
        val getFoodByCategoryIdUseCase by closestDI().instance<GetFoodByCategoryIdUseCase>()

        val categoryId = call.parameters["categoryId"]
        val resource = getFoodByCategoryIdUseCase(categoryId)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    get("/api/food/getFoodAndCategoryGroupAll") {
        val getFoodAndCategoryGroupAllUseCase by closestDI().instance<GetFoodAndCategoryGroupAllUseCase>()

        val resource = getFoodAndCategoryGroupAllUseCase()
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