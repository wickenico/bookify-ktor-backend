package com.nw.persistence

import com.nw.models.BookTag
import com.nw.models.Tag
import com.nw.models.Tags
import com.nw.utils.tagFacade
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TagRepository : TagFacade {
    private fun resultRowToTag(row: ResultRow) =
        Tag(
            id = row[Tags.id],
            name = row[Tags.name],
            userId = row[Tags.userId],
        )

    suspend fun allTagsForFacade(): List<Tag> =
        DatabaseFactory.dbQuery {
            Tags.selectAll()
                .orderBy(Tags.name)
                .map(::resultRowToTag)
        }

    override suspend fun allTags(userId: Int): List<Tag> =
        DatabaseFactory.dbQuery {
            Tags.select { Tags.userId eq userId }
                .orderBy(Tags.name)
                .map(::resultRowToTag)
        }

    override suspend fun tag(id: Int): Tag? =
        DatabaseFactory.dbQuery {
            Tags.select { Tags.id eq id }
                .map(::resultRowToTag)
                .singleOrNull()
        }

    override suspend fun findTagById(id: List<Int>): List<Tag> =
        DatabaseFactory.dbQuery {
            Tags.select { Tags.id inList id }
                .map(::resultRowToTag)
        }

    override suspend fun findTagByName(
        name: String,
        userId: Int,
    ): Tag? =
        DatabaseFactory.dbQuery {
            Tags.select { Tags.name eq name }
                .andWhere { Tags.userId eq userId }
                .map(::resultRowToTag)
                .singleOrNull()
        }

    override suspend fun addNewTag(
        name: String,
        userId: Int,
    ): Tag? =
        DatabaseFactory.dbQuery {
            val insertStatement =
                Tags.insert {
                    it[Tags.name] = name
                    it[Tags.userId] = userId
                }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTag)
        }

    override suspend fun deleteTag(id: Int): Boolean {
        return transaction {
            Tags.deleteWhere { Tags.id eq id } > 0
        }
    }

    override suspend fun getTagListFromBookTags(bookTags: List<BookTag>): List<Tag> {
        val tagIds = bookTags.map { it.tagId }
        return tagFacade.findTagById(tagIds)
    }
}
