package com.myfood.server.utility.extension

import com.myfood.server.utility.jwt.UserIdPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.pipeline.*

internal val PipelineContext<*, ApplicationCall>.userId: String?
    get() = call.principal<UserIdPrincipal>()?.userId