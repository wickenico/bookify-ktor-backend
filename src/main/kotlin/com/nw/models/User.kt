package com.nw.models

import com.nw.utils.offsetDateTime
import org.jetbrains.exposed.sql.Table
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicInteger

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val isEmailVerified: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val username: String,
    val password: String
) {
    companion object {
        private val idCounter = AtomicInteger()
        fun newUser(
            name: String,
            email: String,
            isEmailVerified: Boolean,
            createdAt: OffsetDateTime,
            updatedAt: OffsetDateTime,
            username: String,
            password: String
        ) = User(
            idCounter.getAndIncrement(),
            name,
            email,
            isEmailVerified,
            createdAt,
            updatedAt,
            username,
            password
        )
    }
}

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    val email = varchar("email", length = 100)
    val isEmailVerified = bool("is_email_verified")
    val createdAt = offsetDateTime("created_at")
    val updatedAt = offsetDateTime("updated_at")
    val username = varchar("username", length = 50)
    val password = varchar("password", length = 100)

    override val primaryKey = PrimaryKey(id)
}
