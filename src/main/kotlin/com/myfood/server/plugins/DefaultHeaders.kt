package com.myfood.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*

internal fun Application.configureDefaultHeaders() {
    install(DefaultHeaders)
}