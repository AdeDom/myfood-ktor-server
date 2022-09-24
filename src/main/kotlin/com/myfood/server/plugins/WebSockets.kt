package com.myfood.server.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*

internal fun Application.configureWebSockets() {
    install(WebSockets)
}