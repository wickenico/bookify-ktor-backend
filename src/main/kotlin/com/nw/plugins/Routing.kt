package com.nw.plugins

import com.nw.enums.PrintTypeEnum
import com.nw.enums.RatingEnum
import com.nw.enums.ReadStatusEnum
import com.nw.models.Book
import com.nw.models.BookTag
import com.nw.persistence.bookFacade
import com.nw.persistence.bookTagFacade
import com.nw.persistence.tagFacade
import com.nw.persistence.userFacade
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
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

fun Application.configureRouting() {
    routing {
        authenticate {
            get("/") {
                call.respondText("Hello World!")
            }

            route("/api/v1/books") {
                get {
                    val userName = call.principal<UserIdPrincipal>()?.name
                    println("username: $userName")
                    val user = userName?.let { it1 -> userFacade.findUserByUsername(it1) }
                    val books = bookFacade.allBooks(user!!.id)
                    call.respond(books)
                }

                get("{id}") {
                    val id = call.parameters.getOrFail<Int>("id").toInt()
                    val book: Book? = bookFacade.book(id)
                    if (book != null) {
                        call.respond(book)
                    }
                }

                post {
                    val book = call.receive<Book>()
                    val newBook: Book? = bookFacade.addNewBook(
                        isbn10 = book.isbn10,
                        isbn13 = book.isbn13,
                        title = book.title,
                        subtitle = book.subtitle,
                        authors = book.authors,
                        publisher = book.publisher,
                        pages = book.pages,
                        imageUrl = book.imageUrl,
                        selfLink = book.selfLink,
                        publishedDate = book.publishedDate,
                        description = book.description,
                        printType = PrintTypeEnum.getByValue(book.printType.toString()),
                        categories = book.categories,
                        maturityRating = book.maturityRating,
                        language = book.language,
                        infoLink = book.infoLink,
                        rating = RatingEnum.getByString(book.rating.toString()),
                        comment = book.comment,
                        readStatus = ReadStatusEnum.getByValue(book.readStatus.toString()),
                        addedOnDate = book.addedOnDate,
                        userId = book.userId
                    )
                    if (newBook != null) {
                        call.respond(HttpStatusCode.Created, newBook)
                    }
                }

                put("/edit/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val book = call.receive<Book>()

                    val edited = bookFacade.editBook(
                        id, book.isbn10, book.isbn13, book.title, book.subtitle, book.authors, book.publisher, book.pages, book.imageUrl, book.selfLink,
                        book.publishedDate, book.description, book.printType, book.categories, book.maturityRating, book.language, book.infoLink, book.rating,
                        book.comment, book.readStatus, book.addedOnDate, book.userId
                    )
                    if (edited) {
                        val updatedBook = bookFacade.book(id)
                        call.respond(HttpStatusCode.OK, updatedBook!!)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                delete("{id}") {
                    val bookId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    if (bookFacade.book(bookId) != null) {
                        val bookDeleted = bookFacade.deleteBook(bookId)
                        if (bookDeleted) {
                            val userName = call.principal<UserIdPrincipal>()?.name
                            val user = userName?.let { it1 -> userFacade.findUserByUsername(it1) }
                            val bookTagsDeleted = bookTagFacade.deleteAllBookTagsByBookIdAndUserId(bookId, user!!.id)
                            if (bookTagsDeleted) {
                            }
                        }
                        call.respondText("Book $bookId successfully removed.", status = HttpStatusCode.Accepted)
                    } else {
                        call.respondText("Book $bookId not found", status = HttpStatusCode.NotFound)
                    }
                }

                get("{id}/tags") {
                    val bookId = call.parameters.getOrFail<Int>("id").toInt()
                    val bookTags: List<BookTag> = bookTagFacade.findAllBookTagsByBookId(bookId)
                    if (bookTags.isNotEmpty()) {
                        val tagList = tagFacade.getTagListFromBookTags(bookTags)
                        call.respond(tagList)
                    } else {
                        call.respond("No Tags for Book $bookId found.")
                    }
                }

                post("{id}/tags/add") {
                    val userName = call.principal<UserIdPrincipal>()?.name
                    val user = userName?.let { it1 -> userFacade.findUserByUsername(it1) }
                    val bookId = call.parameters.getOrFail<Int>("id").toInt()
                    val tagName = call.receive<String>()
                    val bookTag = bookTagFacade.addTagToBook(bookId, tagName, user!!.id)
                    if (bookTag != null) {
                        call.respond(HttpStatusCode.Created, "Tag $tagName added to book.")
                    } else {
                        call.respond(HttpStatusCode.Conflict, "Tag $tagName already assigned to book.")
                    }
                }
            }
        }
    }
}
