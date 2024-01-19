package com.nw.models

import org.jetbrains.exposed.sql.Table

data class BookTag(
    val id: Int,
    val bookId: Int,
    val tagId: Int,
    val userId: Int,
)

object BookTags : Table() {
    val id = integer("id").autoIncrement()
    val bookId = integer("book_id")
    val tagId = integer("tag_id")
    val userId = integer("user_id")

    override val primaryKey = PrimaryKey(BookTags.id)
}
