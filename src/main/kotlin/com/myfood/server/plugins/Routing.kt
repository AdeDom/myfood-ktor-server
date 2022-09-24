package com.myfood.server.plugins

import com.myfood.server.route.http.*
import com.myfood.server.route.web_sockets.chatWebSocketsRoute
import com.myfood.server.route.web_sockets.favoriteWebSocketsRoute
import com.myfood.server.route.web_sockets.ratingScoreWebSocketsRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing) {
        defaultRoute()
        authRoute()
        profileRoute()
        foodRoute()
        categoryRoute()
        favoriteRoute()
        ratingScoreRoute()

        chatWebSocketsRoute()
        favoriteWebSocketsRoute()
        ratingScoreWebSocketsRoute()
    }
}