package com.nw.persistence

import com.nw.models.Tag
import com.nw.models.Tags
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TagRepository : TagFacade {

    private fun resultRowToTag(row: ResultRow) = Tag(
        id = row[Tags.id],
        name = row[Tags.name]
    )

    override suspend fun allTags(): List<Tag> = DatabaseFactory.dbQuery {
        Tags.selectAll().map(::resultRowToTag)
    }

    override suspend fun tag(id: Int): Tag? = DatabaseFactory.dbQuery {
        Tags.select { Tags.id eq id }
            .map(::resultRowToTag)
            .singleOrNull()
    }

    override suspend fun addNewTag(tag: Tag): Tag? {
        return transaction {
            Tags.insert {
                it[name] = tag.name
            }.let {
                tag.copy(id = it[Tags.id])
            }
        }
    }

    override suspend fun deleteTag(id: Int): Boolean {
        return transaction {
            Tags.deleteWhere { Tags.id eq id } > 0
        }
    }
}

val tagFacade: TagFacade = TagRepository().apply {
    runBlocking {
        if (allTags().isEmpty()) {
        }
    }
}
