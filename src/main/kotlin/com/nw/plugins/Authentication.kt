package com.nw.plugins

import com.nw.persistence.userFacade
import com.nw.security.hash
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic

fun Application.configureAuth() {
    install(Authentication) {
        basic {
            realm = "Bookify-ktor-backend"
            validate { credentials ->
                val user = userFacade.findUserByUsername(credentials.name)
                if (user != null && user.password == hash(credentials.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
