package com.nw.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.versioning() {
    routing {
        get("/api/v1/version") {
            call.respondText("0.1.0")
        }
    }
}
