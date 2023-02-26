package com.myfood.server.route.http

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.usecase.rating_score.DeleteRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.GetRatingScoreAllUseCase
import com.myfood.server.usecase.rating_score.MyRatingScoreUseCase
import com.myfood.server.usecase.rating_score.SyncDataRatingScoreUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Route.ratingScoreRoute() {

    val getRatingScoreAllUseCase by inject<GetRatingScoreAllUseCase>()
    get("/api/rating/getRatingScoreAll") {
        try {
            val result = getRatingScoreAllUseCase()
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val myRatingScoreUseCase by inject<MyRatingScoreUseCase>()
    post("/api/rating/myRatingScore") {
        val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
        val myRatingScoreRequest = call.receive<MyRatingScoreRequest>()
        try {
            val result = myRatingScoreUseCase(authKey, myRatingScoreRequest)
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val deleteRatingScoreAllUseCase by inject<DeleteRatingScoreAllUseCase>()
    delete("/api/rating/deleteAll") {
        try {
            val result = deleteRatingScoreAllUseCase()
            val response = BaseResponse(
                status = ResponseKeyConstant.SUCCESS,
                result = result,
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: ApplicationException) {
            call.respond(HttpStatusCode.BadRequest, e.toBaseError())
        }
    }

    val syncDataRatingScoreUseCase by inject<SyncDataRatingScoreUseCase>()
    post("/api/rating/syncDataRatingScore") {
        try {
            val result = syncDataRatingScoreUseCase()
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