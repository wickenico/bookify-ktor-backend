package com.nw.plugins

import com.nw.models.User
import com.nw.persistence.userFacade
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail

fun Application.configureUser() {
    routing {
        route("/api/v1/users") {
            get {
                val users = userFacade.allUsers()
                call.respond(users)
            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val user: User? = userFacade.user(id)
                if (user != null) {
                    call.respond(user)
                }
            }
            delete("{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (userFacade.user(id) != null) {
                    userFacade.deleteUser(id)
                    call.respondText("User $id successfully removed.", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("User $id not found", status = HttpStatusCode.NotFound)
                }
            }
        }
        route("/api/v1/registration") {
            post {
                val user = call.receive<User>()
                userFacade.addNewUser(user)
                call.respond(HttpStatusCode.Created, user)
            }
        }
    }
}
