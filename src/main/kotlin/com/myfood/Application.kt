package com.myfood

import com.myfood.server.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDefaultHeaders()
    configureCallLogging()
    configureContentNegotiation()
    configureWebSockets()
    configureKodein()
    configureRouting()
}
