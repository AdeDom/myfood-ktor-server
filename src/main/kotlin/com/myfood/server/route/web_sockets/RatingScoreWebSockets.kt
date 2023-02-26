package com.myfood.server.route.web_sockets

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.usecase.rating_score.MyRatingScoreUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
import com.myfood.server.utility.constant.ResponseKeyConstant
import com.myfood.server.utility.exception.ApplicationException
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.util.*

internal fun Route.ratingScoreWebSocketsRoute() {

    val myRatingScoreUseCase by inject<MyRatingScoreUseCase>()
    val myRatingScoreConnections = Collections.synchronizedSet<DefaultWebSocketSession>(LinkedHashSet())
    webSocket("/ws/rating/myRatingScore") {
        myRatingScoreConnections += this
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val text = frame.readText()

                val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
                val myRatingScoreRequest = Json.decodeFromString<MyRatingScoreRequest>(text)
                try {
                    val result = myRatingScoreUseCase(authKey, myRatingScoreRequest)
                    val response = BaseResponse(
                        status = ResponseKeyConstant.SUCCESS,
                        result = result,
                    )
                    val json = Json.encodeToString(response)
                    myRatingScoreConnections.forEach { session ->
                        session.send(json)
                    }
                } catch (e: ApplicationException) {
                    val json = Json.encodeToString(e.toBaseError())
                    this.send(json)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.localizedMessage)
        } finally {
            myRatingScoreConnections -= this
        }
    }
}