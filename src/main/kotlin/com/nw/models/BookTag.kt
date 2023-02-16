package com.nw.models

import org.jetbrains.exposed.sql.Table

data class BookTag(
    val bookId: Int,
    val tagId: Int
)

object BookTags : Table() {
    val bookId = integer("book_id").references(Books.id)
    val tagId = integer("tag_id")
}
