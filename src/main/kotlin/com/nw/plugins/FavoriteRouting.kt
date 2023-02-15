package com.nw.plugins

import com.nw.models.Favorite
import com.nw.persistence.favoriteFacade
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

fun Application.configureFavorites() {
    routing {
        authenticate {
            route("/api/v1/favorites") {
                get {
                    val favorites = favoriteFacade.allFavorites()
                    call.respond(favorites)
                }

                get("{favoriteId}") {
                    val favId = call.parameters.getOrFail<Int>("favoriteId").toInt()
                    val favorite = favoriteFacade.findFavoriteById(favId)
                    if (favorite != null) {
                        call.respond(favorite)
                    }
                }

                get("{userId}") {
                    val userId = call.parameters.getOrFail<Int>("userId").toInt()
                    val favoriteList = favoriteFacade.findAllFavoritesByUserId(userId)
                    call.respond(favoriteList)
                }

                post {
                    val favorite = call.receive<Favorite>()
                    val newFavorite: Favorite? = favoriteFacade.addNewFavorite(favorite)
                    if (newFavorite != null) {
                        call.respond(HttpStatusCode.Created, newFavorite)
                    }
                }

                delete {
                    val favorite = call.receive<Favorite>()
                    favoriteFacade.deleteFavorite(favorite.userId, favorite.bookId)
                    call.respondText("Favorite unmarked.", status = HttpStatusCode.Accepted)
                }
            }
        }
    }
}
