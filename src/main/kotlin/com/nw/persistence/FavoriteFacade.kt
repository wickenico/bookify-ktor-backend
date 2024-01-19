package com.nw.persistence

import com.nw.models.Favorite

interface FavoriteFacade {
    suspend fun allFavorites(): List<Favorite>

    suspend fun addNewFavorite(favorite: Favorite): Favorite?

    suspend fun deleteFavorite(
        userId: Int,
        bookId: Int,
    ): Boolean

    suspend fun findFavoriteById(id: Int): Favorite?

    suspend fun findAllFavoritesByUserId(userId: Int): List<Favorite>

    suspend fun isBookMarkedAsFavorite(
        userId: Int,
        bookId: Int,
    ): Boolean
}
