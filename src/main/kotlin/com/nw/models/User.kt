package com.nw.models

import com.nw.utils.offsetDateTime
import org.jetbrains.exposed.sql.Table
import java.time.OffsetDateTime

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val username: String,
    val password: String
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val fullName = varchar("full_name", length = 50)
    val email = varchar("email", length = 100)
    val createdAt = offsetDateTime("created_at").clientDefault { OffsetDateTime.now() }
    val username = varchar("username", length = 50)
    val password = varchar("password", length = 100)

    override val primaryKey = PrimaryKey(id)
}
