package com.nw.persistence

import com.nw.models.BookTag
import com.nw.models.BookTags
import com.nw.utils.tagFacade
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class BookTagRepository : BookTagFacade {
    private fun resultRowToBookTag(row: ResultRow) =
        BookTag(
            id = row[BookTags.id],
            bookId = row[BookTags.bookId],
            tagId = row[BookTags.tagId],
            userId = row[BookTags.userId],
        )

    override suspend fun getAllBookTags(): List<BookTag> =
        DatabaseFactory.dbQuery {
            BookTags.selectAll().map(::resultRowToBookTag)
        }

    override suspend fun addTagToBook(
        bookId: Int,
        tagName: String,
        userId: Int,
    ): BookTag? =
        DatabaseFactory.dbQuery {
            val tag = tagFacade.findTagByName(tagName, userId)
            val tagId: Int =
                if (tag == null) {
                    tagFacade.addNewTag(tagName, userId)
                    tagFacade.findTagByName(tagName, userId)!!.id
                } else {
                    tag.id
                }

            if (!checkIfBookTagExists(bookId, tagId, userId)) {
                val insertStatement =
                    BookTags.insert {
                        it[BookTags.bookId] = bookId
                        it[BookTags.tagId] = tagId
                        it[BookTags.userId] = userId
                    }

                insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBookTag)
            } else {
                null
            }
        }

    override suspend fun findAllBookTagsByBookId(bookId: Int): List<BookTag> =
        DatabaseFactory.dbQuery {
            BookTags.select { BookTags.bookId eq bookId }
                .map(::resultRowToBookTag)
        }

    override suspend fun findAllBookTagsByTagId(tagId: Int): List<BookTag> =
        DatabaseFactory.dbQuery {
            BookTags.select { BookTags.tagId eq tagId }
                .map { resultRowToBookTag(it) }
        }

    private suspend fun checkIfBookTagExists(
        bookId: Int,
        tagId: Int,
        userId: Int,
    ): Boolean {
        return DatabaseFactory.dbQuery {
            BookTags.select { BookTags.bookId eq bookId }
                .andWhere { BookTags.tagId eq tagId }
                .andWhere { BookTags.userId eq userId }
                .count() > 0
        }
    }

    override suspend fun deleteBookTagByBookIdAndTagIdAndUserId(
        bookId: Int,
        tagId: Int,
        userId: Int,
    ): Boolean {
        return DatabaseFactory.dbQuery {
            BookTags.deleteWhere {
                (BookTags.bookId eq bookId) and (BookTags.tagId eq tagId) and (BookTags.userId eq userId)
            } > 0
        }
    }

    override suspend fun deleteAllBookTagsByBookIdAndUserId(
        bookId: Int,
        userId: Int,
    ): Boolean {
        return DatabaseFactory.dbQuery {
            BookTags.deleteWhere {
                (BookTags.bookId eq bookId) and (BookTags.userId eq userId)
            } > 0
        }
    }

    override suspend fun deleteBookTagById(id: Int): Boolean {
        return DatabaseFactory.dbQuery {
            BookTags.deleteWhere { (BookTags.id eq id) } > 0
        }
    }

    override suspend fun countBookTagsByTagId(
        tagId: Int,
        userId: Int,
    ): Long {
        return DatabaseFactory.dbQuery {
            BookTags.select { (BookTags.tagId eq tagId) and (BookTags.userId eq userId) }
                .count()
        }
    }
}
