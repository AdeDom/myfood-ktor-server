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
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.ratingScoreRoute() {

    get("/api/rating/getRatingScoreAll") {
        val getRatingScoreAllUseCase by closestDI().instance<GetRatingScoreAllUseCase>()

        val resource = getRatingScoreAllUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/rating/myRatingScore") {
        val myRatingScoreUseCase by closestDI().instance<MyRatingScoreUseCase>()

        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myRatingScoreRequest = call.receive<MyRatingScoreRequest>()
        val resource = myRatingScoreUseCase(authKey, myRatingScoreRequest)
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    delete("/api/rating/deleteAll") {
        val deleteRatingScoreAllUseCase by closestDI().instance<DeleteRatingScoreAllUseCase>()

        val resource = deleteRatingScoreAllUseCase()
        when (resource) {
            is Resource.Success -> {
                call.respond(HttpStatusCode.OK, resource.data)
            }

            is Resource.Error -> {
                call.respond(HttpStatusCode.BadRequest, resource.error)
            }
        }
    }

    post("/api/rating/syncDataRatingScore") {
        val syncDataRatingScoreUseCase by closestDI().instance<SyncDataRatingScoreUseCase>()

        val resource = syncDataRatingScoreUseCase()
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