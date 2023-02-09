package com.nw.models

import org.jetbrains.exposed.sql.Table

data class UserBook(
    val userId: Int,
    val bookId: Int,
)

object UserBooks : Table() {
    val userId = integer("user_id").references(Users.id)
    val bookId = integer("book_id").references(Books.id)
}
