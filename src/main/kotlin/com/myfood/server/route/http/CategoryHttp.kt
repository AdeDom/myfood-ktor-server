package com.myfood.server.route.http

import com.myfood.server.data.models.request.InsertCategoryRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.category.GetCategoryAllUseCase
import com.myfood.server.usecase.category.InsertCategoryUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.categoryRoute() {

    val insertCategoryUseCase by inject<InsertCategoryUseCase>()
    post("/api/category/insert") {
        val request = call.receive<InsertCategoryRequest>()
        when (val resource = insertCategoryUseCase(request)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val getCategoryAllUseCase by inject<GetCategoryAllUseCase>()
    get("/api/category/getCategoryAll") {
        when (val resource = getCategoryAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}