package com.nw.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respondText(text = "405: Method Not Allowed", status = status)
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Not Found", status = status)
        }
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respondText(text = "401: Unauthorized request.", status = status)
        }
    }
}
