package com.myfood.server.route.web_sockets

import com.myfood.server.data.models.request.MyRatingScoreRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.rating_score.MyRatingScoreUseCase
import com.myfood.server.utility.constant.RequestKeyConstant
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
                val resource = myRatingScoreUseCase(authKey, myRatingScoreRequest)
                when (resource) {
                    is Resource.Success -> {
                        val response = Json.encodeToString(resource.data)
                        myRatingScoreConnections.forEach { session ->
                            session.send(response)
                        }
                    }

                    is Resource.Error -> {
                        val response = Json.encodeToString(resource.error)
                        this.send(response)
                    }
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