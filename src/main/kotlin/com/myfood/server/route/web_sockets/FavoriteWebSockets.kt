package com.myfood.server.route.web_sockets

import com.myfood.server.data.models.request.MyFavoriteRequest
import com.myfood.server.data.repositories.Resource
import com.myfood.server.usecase.favorite.MyFavoriteUseCase
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

fun Route.favoriteWebSocketsRoute() {

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
                val resource = myFavoriteUseCase(authKey, myFavoriteRequest)
                when (resource) {
                    is Resource.Success -> {
                        val response = Json.encodeToString(resource.data)
                        myFavoriteConnections.forEach { session ->
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
            myFavoriteConnections -= this
        }
    }
}