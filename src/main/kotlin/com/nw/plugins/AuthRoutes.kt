package com.nw.plugins

import com.nw.jwtConfig
import com.nw.models.User
import com.nw.persistence.userFacade
import com.nw.security.JwtConfig
import com.nw.security.LoginBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.authRoutes() {

    routing {
        route("/api/v1/auth") {
            post("/register") {
                val user: User = call.receive()
                userFacade.addNewUser(user)
                call.respond(HttpStatusCode.Created, user)
            }

            post("/login") {
                val loginBody = call.receive<LoginBody>()
                val user = userFacade.getUser(loginBody.username, loginBody.password)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid Credentials")
                }

                val token = jwtConfig.generateToken(JwtConfig.JwtUser(userId = user!!.id, userName = user.name, email = user.email))
                call.respond(token)
            }

            authenticate {
                get("/me") {
                    val user = call.authentication.principal as JwtConfig.JwtUser
                    call.respond(user)
                }
            }
        }
    }
}
