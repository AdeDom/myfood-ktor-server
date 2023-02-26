package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.usecase.favorite.DeleteFavoriteAllUseCase
import com.myfood.server.usecase.favorite.GetFavoriteAllUseCase
import com.myfood.server.usecase.favorite.MyFavoriteUseCase
import com.myfood.server.usecase.favorite.SyncDataFavoriteUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.favoriteRoute() {

    val getFavoriteAllUseCase by inject<GetFavoriteAllUseCase>()
    get("/api/favorite/getFavoriteAll") {
        try {
            val result = getFavoriteAllUseCase()
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val myFavoriteUseCase by inject<MyFavoriteUseCase>()
    post("/api/favorite/myFavorite") {
        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myFavoriteRequest = call.receive<MyFavoriteRequest>()
        try {
            val result = myFavoriteUseCase(authKey, myFavoriteRequest)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val deleteFavoriteAllUseCase by inject<DeleteFavoriteAllUseCase>()
    delete("/api/favorite/deleteAll") {
        try {
            val result = deleteFavoriteAllUseCase()
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val syncDataFavoriteUseCase by inject<SyncDataFavoriteUseCase>()
    post("/api/favorite/syncDataFavorite") {
        try {
            val result = syncDataFavoriteUseCase()
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