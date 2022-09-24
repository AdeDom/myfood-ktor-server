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
import org.koin.ktor.ext.inject

internal fun Route.favoriteRoute() {

    val getFavoriteAllUseCase by inject<GetFavoriteAllUseCase>()
    get("/api/favorite/getFavoriteAll") {
        when (val resource = getFavoriteAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val myFavoriteUseCase by inject<MyFavoriteUseCase>()
    post("/api/favorite/myFavorite") {
        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myFavoriteRequest = call.receive<MyFavoriteRequest>()
        when (val resource = myFavoriteUseCase(authKey, myFavoriteRequest)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val deleteFavoriteAllUseCase by inject<DeleteFavoriteAllUseCase>()
    delete("/api/favorite/deleteAll") {
        when (val resource = deleteFavoriteAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val syncDataFavoriteUseCase by inject<SyncDataFavoriteUseCase>()
    post("/api/favorite/syncDataFavorite") {
        when (val resource = syncDataFavoriteUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}