package com.nw.persistence

import com.nw.models.UserBook
import com.nw.models.UserBooks
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserBookRepository : UserBookFacade {

    private fun resultRowToUserBook(row: ResultRow) = UserBook(
        userId = row[UserBooks.userId],
        bookId = row[UserBooks.bookId],
    )

    override suspend fun getAllUserBooks(): List<UserBook> = DatabaseFactory.dbQuery {
        UserBooks.selectAll().map { resultRowToUserBook(it) }
    }

    override suspend fun findAllUserBooksByUserId(userId: Int): List<UserBook> = DatabaseFactory.dbQuery {
        UserBooks.select { UserBooks.userId eq userId }
            .map { resultRowToUserBook(it) }
    }

    override suspend fun addBookToUser(userId: Int, bookId: Int): UserBook? = DatabaseFactory.dbQuery {

        if (!checkIfUserBookExists(userId, bookId)) {
            val insertStatement = UserBooks.insert {
                it[UserBooks.userId] = userId
                it[UserBooks.bookId] = bookId
            }
            insertStatement.resultedValues?.singleOrNull()?.let { resultRowToUserBook(it) }
        } else null
    }

    private suspend fun checkIfUserBookExists(userId: Int, bookId: Int): Boolean {
        return DatabaseFactory.dbQuery {
            UserBooks.select { UserBooks.userId eq userId }
                .andWhere { UserBooks.bookId eq bookId }
                .count() > 0
        }
    }
}

val userBookFacade: UserBookFacade = UserBookRepository().apply {
    runBlocking {
        if (getAllUserBooks().isEmpty()) {
        }
    }
}
