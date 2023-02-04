package com.nw.persistence

import com.nw.models.User
import com.nw.models.Users
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
            name = row[Users.name],
            email = row[Users.email],
            isEmailVerified = row[Users.isEmailVerified],
            createdAt = row[Users.createdAt],
            updatedAt = row[Users.updatedAt],
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
                it[name] = user.name
                it[email] = user.email
                it[isEmailVerified] = user.isEmailVerified
                it[createdAt] = user.createdAt
                it[updatedAt] = user.updatedAt
                it[username] = user.username
                it[password] = user.password
            }.let {
                user.copy(id = it[Users.id])
            }
        }
    }

    override suspend fun editUser(user: User): Boolean {
        return transaction {
            Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[email] = user.email
                it[isEmailVerified] = user.isEmailVerified
                it[updatedAt] = user.updatedAt
                it[username] = user.username
                it[password] = user.password
            }.let { it > 0 }
        }
    }

    override suspend fun deleteUser(id: Int): Boolean {
        return transaction {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }
}

val userFacade: UserFacade = UserRepository().apply {
    runBlocking {
        if (allUsers().isEmpty()) {
        }
    }
}
