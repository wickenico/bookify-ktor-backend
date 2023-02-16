package com.nw.persistence

import com.nw.models.Favorite
import com.nw.models.Favorites
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FavoriteRepository : FavoriteFacade {

    private fun resultRowToFavorite(row: ResultRow) = Favorite(
        id = row[Favorites.id],
        userId = row[Favorites.userId],
        bookId = row[Favorites.bookId]
    )

    override suspend fun allFavorites(): List<Favorite> = DatabaseFactory.dbQuery {
        Favorites.selectAll().map(::resultRowToFavorite)
    }

    override suspend fun addNewFavorite(favorite: Favorite): Favorite? = DatabaseFactory.dbQuery {
        val insertStatement = Favorites.insert {
            it[Favorites.userId] = favorite.userId
            it[Favorites.bookId] = favorite.bookId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToFavorite)
    }

    override suspend fun deleteFavorite(userId: Int, bookId: Int): Boolean {
        return transaction {
            Favorites.deleteWhere {
                (Favorites.userId eq userId) and (Favorites.bookId eq bookId)
            } > 0
        }
    }

    override suspend fun findFavoriteById(id: Int): Favorite? = DatabaseFactory.dbQuery {
        Favorites.select { Favorites.id eq id }
            .map { resultRowToFavorite(it) }
            .singleOrNull()
    }

    override suspend fun findAllFavoritesByUserId(userId: Int): List<Favorite> = DatabaseFactory.dbQuery {
        Favorites.select { Favorites.userId eq userId }
            .map(::resultRowToFavorite)
    }

    override suspend fun isBookMarkedAsFavorite(userId: Int, bookId: Int): Boolean = DatabaseFactory.dbQuery {
        Favorites.select {
            (Favorites.userId eq userId) and (Favorites.bookId eq bookId)
        }.map(::resultRowToFavorite)
            .singleOrNull() != null
    }
}

val favoriteFacade: FavoriteFacade = FavoriteRepository().apply {
    runBlocking {
        if (allFavorites().isEmpty()) {
        }
    }
}
