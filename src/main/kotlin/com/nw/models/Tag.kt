package com.nw.models

import org.jetbrains.exposed.sql.Table
import java.util.concurrent.atomic.AtomicInteger

data class Tag(
    val id: Int,
    val name: String
) {
    companion object {
        private val idCounter = AtomicInteger()
        fun newTag(
            name: String
        ) = Tag(
            idCounter.getAndIncrement(),
            name
        )
    }
}

object Tags : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)

    override val primaryKey = PrimaryKey(Tags.id)
}
