package com.nw.plugins

import com.nw.models.PasswordChange
import com.nw.models.User
import com.nw.models.UserBook
import com.nw.models.UserEdit
import com.nw.persistence.BookFacade
import com.nw.persistence.UserFacade
import com.nw.persistence.userBookFacade
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail

fun Application.configureUser(
    bookFacade: BookFacade,
    userFacade: UserFacade,
) {
    routing {
        authenticate {
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
                get("details") {
                    val username = call.request.queryParameters.getOrFail("username")
                    val user: User? = userFacade.findUserByUsername(username)
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

                get("{id}/books") {
                    val userId = call.parameters.getOrFail<Int>("id").toInt()
                    val userBooks: List<UserBook> = userBookFacade.findAllUserBooksByUserId(userId)
                    if (userBooks.isNotEmpty()) {
                        val bookList = userFacade.getBookListFromUserBooks(userBooks)
                        call.respond(bookList)
                    } else {
                        call.respond("No Books for User $userId found.")
                    }
                }

                post("{id}/books/add") {
                    val userId = call.parameters.getOrFail<Int>("id").toInt()
                    val bookIdAsString = call.receive<String>()
                    val intValue = bookIdAsString.toIntOrNull()
                    if (intValue == null) {
                        call.respond(HttpStatusCode.BadRequest, "Request body is not a valid integer.")
                    }
                    val bookId = bookIdAsString.toInt()
                    if (bookFacade.book(bookId) == null) {
                        call.respond(HttpStatusCode.Conflict, "Book $bookId not exists.")
                    }
                    val userBookRelation = userBookFacade.addBookToUser(userId, bookId)
                    if (userBookRelation != null) {
                        call.respond(HttpStatusCode.Created, "Book $bookId added to user.")
                    } else {
                        call.respond(HttpStatusCode.Conflict, "Book $bookId already assigned to user.")
                    }
                }

                put("/edit/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val editUser = call.receive<UserEdit>()

                    val edited =
                        userFacade.editUser(
                            id,
                            editUser.fullName,
                            editUser.email,
                        )

                    if (edited) {
                        val updatedUser = userFacade.user(id)
                        call.respond(HttpStatusCode.OK, updatedUser!!)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                put("/edit/{id}/password") {
                    val id = call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val passwordDto = call.receive<PasswordChange>()

                    if (userFacade.verifyUser(id, passwordDto.oldPassword)) {
                        userFacade.changePassword(id, passwordDto.newPassword)
                        call.respond(HttpStatusCode.OK, "User $id updated.")
                    } else {
                        call.respond(HttpStatusCode.Conflict, "Password for user $id not correct.")
                    }
                }
            }
        }
    }
}
