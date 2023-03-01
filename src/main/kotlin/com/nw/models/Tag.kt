package com.nw.models

import org.jetbrains.exposed.sql.Table
import java.util.concurrent.atomic.AtomicInteger

data class Tag(
    val id: Int,
    val name: String,
    val userId: Int
) {
    companion object {
        private val idCounter = AtomicInteger()
        fun newTag(
            name: String,
            userId: Int
        ) = Tag(
            idCounter.getAndIncrement(),
            name,
            userId

        )
    }
}

object Tags : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    val userId = integer("user_id")

    override val primaryKey = PrimaryKey(Tags.id)
}
