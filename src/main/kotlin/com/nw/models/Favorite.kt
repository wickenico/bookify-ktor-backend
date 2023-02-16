package com.nw.models

import org.jetbrains.exposed.sql.Table

data class Favorite(
    val id: Int,
    val userId: Int,
    val bookId: Int
)

object Favorites : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val bookId = integer("book_id")

    override val primaryKey = PrimaryKey(id)
}
