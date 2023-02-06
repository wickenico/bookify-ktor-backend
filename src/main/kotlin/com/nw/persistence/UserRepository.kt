package com.nw.persistence

import com.nw.jwtConfig
import com.nw.models.User
import com.nw.models.Users
import com.nw.security.JwtConfig
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
            password = row[Users.password],
            authToken = row[Users.authToken],
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
                val token = jwtConfig.generateToken(JwtConfig.JwtUser(userId = user.id, userName = user.username, email = user.email))
                it[authToken] = token
            }.let {
                user.copy(id = it[Users.id])
            }
        }
    }

    override suspend fun editUser(user: User): Boolean {
        return transaction {
            Users.update({ Users.id eq user.id }) {
                it[fullName] = user.fullName
                it[email] = user.email
                it[username] = user.username
                it[password] = user.password
                it[authToken] = user.authToken
            }.let { it > 0 }
        }
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
}

val userFacade: UserFacade = UserRepository().apply {
    runBlocking {
        if (allUsers().isEmpty()) {
        }
    }
}
