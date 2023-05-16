package com.nw.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.BadRequest) { call, status ->
            call.respondText(text = "400: Bad Request", status = status)
        }
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respondText(text = "401: Unauthorized request.", status = status)
        }
        status(HttpStatusCode.Forbidden) { call, status ->
            call.respondText(text = "403: Forbidden", status = status)
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Not Found", status = status)
        }
        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respondText(text = "405: Method Not Allowed", status = status)
        }
        status(HttpStatusCode.Conflict) { call, status ->
            call.respondText(text = "409: Conflict", status = status)
        }
        status(HttpStatusCode.UnprocessableEntity) { call, status ->
            call.respondText(text = "422: Unprocessable Entity", status = status)
        }
        status(HttpStatusCode.TooManyRequests) { call, status ->
            call.respondText(text = "429: Too Many Requests", status = status)
        }
        status(HttpStatusCode.InternalServerError) { call, status ->
            call.respondText(text = "500: Internal Server Error", status = status)
        }
        status(HttpStatusCode.ServiceUnavailable) { call, status ->
            call.respondText(text = "503: Service Unavailable", status = status)
        }
        status(HttpStatusCode.GatewayTimeout) { call, status ->
            call.respondText(text = "504: Gateway Timeout", status = status)
        }
    }
}
