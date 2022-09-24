package com.myfood.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*

fun Application.configureCallLogging() {
    install(CallLogging)
}