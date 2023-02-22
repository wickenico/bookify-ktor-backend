package com.nw.plugins

import com.nw.models.User
import com.nw.persistence.userFacade
import com.nw.security.LoginBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sessions.set

fun Application.authRoutes() {
    routing {
        route("/api/v1/auth") {
            post("/register") {
                val user: User = call.receive()
                if (userFacade.checkIfUsernameExists(user.username)) {
                    call.respond(HttpStatusCode.Conflict, "Username already taken.")
                } else {
                    val newUser = userFacade.addNewUser(user)
                    call.respond(HttpStatusCode.Created, newUser!!)
                }
            }

            post("/login") {
                val loginBody = call.receive<LoginBody>()
                val user = userFacade.getUser(loginBody.username, loginBody.password)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid Credentials")
                } else {
                    // call.sessions.set(UserSession(id = user.id.toString(), count = 5))
                    call.respond(user)
                }
            }

            authenticate {
                get("/me") {
                    val user = call.principal<Principal>()
                    if (user != null) {
                        call.respond(user)
                    }
                }
            }
        }
    }
}
