package com.myfood.server.route.http

import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.favorite.DeleteFavoriteAllUseCase
import com.myfood.server.usecase.favorite.GetFavoriteAllUseCase
import com.myfood.server.usecase.favorite.MyFavoriteUseCase
import com.myfood.server.usecase.favorite.SyncDataFavoriteUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.favoriteRoute() {

    get("/api/favorite/getFavoriteAll") {
        val getFavoriteAllUseCase by closestDI().instance<GetFavoriteAllUseCase>()

        val resource = getFavoriteAllUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/favorite/myFavorite") {
        val myFavoriteUseCase by closestDI().instance<MyFavoriteUseCase>()

        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myFavoriteRequest = call.receive<MyFavoriteRequest>()
        val resource = myFavoriteUseCase(authKey, myFavoriteRequest)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    delete("/api/favorite/deleteAll") {
        val deleteFavoriteAllUseCase by closestDI().instance<DeleteFavoriteAllUseCase>()

        val resource = deleteFavoriteAllUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/favorite/syncDataFavorite") {
        val syncDataFavoriteUseCase by closestDI().instance<SyncDataFavoriteUseCase>()

        val resource = syncDataFavoriteUseCase()
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