package com.nw.plugins

import com.nw.models.BookTag
import com.nw.models.Tag
import com.nw.persistence.bookFacade
import com.nw.persistence.bookTagFacade
import com.nw.persistence.tagFacade
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
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail

fun Application.configureTag() {
    routing {
        authenticate {
            route("api/v1/tags") {
                get {
                    val tags = tagFacade.allTags()
                    call.respond(tags)
                }
                get("{id}") {
                    val id = call.parameters.getOrFail<Int>("id").toInt()
                    val tag: Tag? = tagFacade.tag(id)
                    if (tag != null) {
                        call.respond(tag)
                    }
                }
                post {
                    val tagName = call.receive<String>()
                    val tag: Tag? = tagFacade.addNewTag(tagName)
                    if (tag != null) {
                        call.respond(HttpStatusCode.Created, tag)
                    }
                }

                delete("{id}") {
                    val id = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    if (tagFacade.tag(id) != null) {
                        tagFacade.deleteTag(id)
                        call.respondText("Tag $id successfully removed.", status = HttpStatusCode.Accepted)
                    } else {
                        call.respondText("Tag $id not found", status = HttpStatusCode.NotFound)
                    }
                }

                get("{id}/books") {
                    val tagId = call.parameters.getOrFail<Int>("id").toInt()
                    val bookTagList: List<BookTag> = bookTagFacade.findAllBookTagsByTagId(tagId)
                    if (bookTagList.isNotEmpty()) {
                        val bookList = bookFacade.getBookLIstFromBookTags(bookTagList)
                        call.respond(bookList)
                    } else {
                        call.respond("No Books for Tag $tagId found.")
                    }
                }
            }
        }
    }
}
