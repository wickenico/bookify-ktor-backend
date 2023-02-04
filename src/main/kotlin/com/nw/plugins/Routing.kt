package com.nw.plugins

import com.nw.models.Book
import com.nw.persistence.bookFacade
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        route("/api/v1/books") {
            get {
                val books = bookFacade.allBooks()
                call.respond(books)
            }

            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val book: Book? = bookFacade.book(id)
                if (book != null) {
                    call.respond(book)
                }
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (bookFacade.book(id) != null) {
                    bookFacade.deleteBook(id)
                    call.respondText("Book $id successfully removed.", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Book $id not found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}
