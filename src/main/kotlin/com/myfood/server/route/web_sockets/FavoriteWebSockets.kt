package com.myfood.server.route.web_sockets

import com.myfood.server.data.models.base.BaseResponse
import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.usecase.favorite.MyFavoriteUseCase
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

internal fun Route.favoriteWebSocketsRoute() {

    val myFavoriteUseCase by inject<MyFavoriteUseCase>()
    val myFavoriteConnections = Collections.synchronizedSet<DefaultWebSocketSession>(LinkedHashSet())
    webSocket("/ws/favorite/myFavorite") {
        myFavoriteConnections += this
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val text = frame.readText()

                val authKey = call.request.header(RequestKeyConstant.AUTHORIZATION_KEY)
                val myFavoriteRequest = Json.decodeFromString<MyFavoriteRequest>(text)
                try {
                    val result = myFavoriteUseCase(authKey, myFavoriteRequest)
                    val response = BaseResponse(
                        status = ResponseKeyConstant.SUCCESS,
                        result = result,
                    )
                    val json = Json.encodeToString(response)
                    myFavoriteConnections.forEach { session ->
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
            myFavoriteConnections -= this
        }
    }
}