package com.myfood.server.utility.jwt

import io.ktor.server.auth.*

internal data class UserIdPrincipal(val userId: String) : Principal