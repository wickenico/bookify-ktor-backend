package com.nw.persistence

import com.nw.models.Book
import com.nw.models.User
import com.nw.models.UserBook
import com.nw.models.Users
import com.nw.security.hash
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserRepository : UserFacade {

    private fun resultRowToUser(row: ResultRow) =
        User(
            id = row[Users.id],
            fullName = row[Users.fullName],
            email = row[Users.email],
            username = row[Users.username],
            password = row[Users.password]
        )

    override suspend fun allUsers(): List<User> = DatabaseFactory.dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun user(id: Int): User? = DatabaseFactory.dbQuery {
        Users.select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun findUserByUsername(username: String): User? {
        return DatabaseFactory.dbQuery {
            Users.select { Users.username eq username }
                .map(::resultRowToUser)
                .singleOrNull()
        }
    }

    override suspend fun findUserByUsernameAndPassword(user: String, password: String): User? =
        DatabaseFactory.dbQuery {
            Users.select { Users.username eq user }
                .andWhere { Users.password eq password }
                .map(::resultRowToUser)
                .singleOrNull()
        }

    override suspend fun addNewUser(user: User): User? {
        return transaction {
            Users.insert {
                it[fullName] = user.fullName
                it[email] = user.email
                it[username] = user.username
                it[password] = hash(password = user.password)
            }.let {
                user.copy(id = it[Users.id])
            }
        }
    }

    override suspend fun editUser(userId: Int, fullName: String, email: String): Boolean = DatabaseFactory.dbQuery {
        Users.update({ Users.id eq userId }) {
            it[Users.fullName] = fullName
            it[Users.email] = email
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean {
        return transaction {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }

    override suspend fun getUser(username: String, password: String): User? = DatabaseFactory.dbQuery {
        Users.select { Users.username eq username }
            .andWhere { Users.password eq hash(password) }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun checkIfUsernameExists(username: String): Boolean {
        return DatabaseFactory.dbQuery {
            val count = Users.select { Users.username eq username }
                .count()

            count > 0
        }
    }

    override suspend fun getBookListFromUserBooks(userBooks: List<UserBook>): List<Book> {
        val bookIds = userBooks.map { it.bookId }
        return bookFacade.findBookById(bookIds)
    }

    override suspend fun verifyUser(userId: Int, password: String): Boolean = DatabaseFactory.dbQuery {
        Users.select { Users.id eq userId }
            .andWhere { Users.password eq hash(password) }
            .singleOrNull() != null
    }

    override suspend fun changePassword(userId: Int, newPassword: String): Boolean = DatabaseFactory.dbQuery {
        Users.update({ Users.id eq userId }) {
            it[Users.password] = hash(newPassword)
        } > 0
    }
}

val userFacade: UserFacade = UserRepository().apply {
    runBlocking {
        if (allUsers().isEmpty()) {
        }
    }
}
