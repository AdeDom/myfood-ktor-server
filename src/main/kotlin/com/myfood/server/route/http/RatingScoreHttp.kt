package com.myfood.server.route.http

import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.rating_score.DeleteRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.GetRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.MyRatingScoreUseCase
import com.myfood.server.usecase.rating_score.SyncDataRatingScoreUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.ratingScoreRoute() {

    val getRatingScoreAllUseCase by inject<GetRatingScoreAllUseCase>()
    get("/api/rating/getRatingScoreAll") {
        when (val resource = getRatingScoreAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val myRatingScoreUseCase by inject<MyRatingScoreUseCase>()
    post("/api/rating/myRatingScore") {
        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myRatingScoreRequest = call.receive<MyRatingScoreRequest>()
        when (val resource = myRatingScoreUseCase(authKey, myRatingScoreRequest)) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val deleteRatingScoreAllUseCase by inject<DeleteRatingScoreAllUseCase>()
    delete("/api/rating/deleteAll") {
        when (val resource = deleteRatingScoreAllUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    val syncDataRatingScoreUseCase by inject<SyncDataRatingScoreUseCase>()
    post("/api/rating/syncDataRatingScore") {
        when (val resource = syncDataRatingScoreUseCase()) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }
}