package com.myfood.server.plugins

import com.auth0.jwt.JWT
import com.myfood.server.utility.jwt.JwtHelper
import com.myfood.server.utility.jwt.UserIdPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

internal fun Application.configureAuthentication(realmEnv: String) {
    install(Authentication) {
        bearer {
            realm = realmEnv
            authenticate { tokenCredential ->
                val userId = JWT().decodeJwt(tokenCredential.token).getClaim(JwtHelper.USER_ID).asString()
                if (userId != null) UserIdPrincipal(userId) else null
            }
        }
    }
}