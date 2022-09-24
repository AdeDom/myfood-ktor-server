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
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.categoryRoute() {

    post("/api/category/insert") {
        val insertCategoryUseCase by closestDI().instance<InsertCategoryUseCase>()

        val request = call.receive<InsertCategoryRequest>()
        val resource = insertCategoryUseCase(request)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    get("/api/category/getCategoryAll") {
        val getCategoryAllUseCase by closestDI().instance<GetCategoryAllUseCase>()

        val resource = getCategoryAllUseCase()
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