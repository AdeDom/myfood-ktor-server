package com.myfood

import com.myfood.server.plugins.*
import io.ktor.server.application.*

internal fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

internal fun Application.module() {
    configureDefaultHeaders()
    configureCallLogging()
    configureContentNegotiation()
    configureWebSockets()
    configureKoin()
    configureRouting()
}
