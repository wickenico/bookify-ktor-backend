package com.nw.persistence

import com.nw.models.BookTag
import com.nw.models.BookTags
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class BookTagRepository : BookTagFacade {

    private fun resultRowToBookTag(row: ResultRow) = BookTag(
        bookId = row[BookTags.bookId],
        tagId = row[BookTags.tagId]
    )

    override suspend fun getAllBookTags(): List<BookTag> = DatabaseFactory.dbQuery {
        BookTags.selectAll().map(::resultRowToBookTag)
    }

    override suspend fun addTagToBook(bookId: Int, tagName: String): BookTag? = DatabaseFactory.dbQuery {
        val tag = tagFacade.findTagByName(tagName)
        val tagId: Int = if (tag == null) {
            tagFacade.addNewTag(tagName)
            tagFacade.findTagByName(tagName)!!.id
        } else {
            tag.id
        }

        if (!checkIfBookTagExists(bookId, tagId)) {
            val insertStatement = BookTags.insert {
                it[BookTags.bookId] = bookId
                it[BookTags.tagId] = tagId
            }

            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBookTag)
        } else {
            null
        }
    }

    override suspend fun findAllBookTagsByBookId(bookId: Int): List<BookTag> = DatabaseFactory.dbQuery {
        BookTags.select { BookTags.bookId eq bookId }
            .map(::resultRowToBookTag)
    }

    private suspend fun checkIfBookTagExists(bookId: Int, tagId: Int): Boolean {
        return DatabaseFactory.dbQuery {
            BookTags.select { BookTags.bookId eq bookId }
                .andWhere { BookTags.tagId eq tagId }
                .count() > 0
        }
    }
}

val bookTagFacade: BookTagFacade = BookTagRepository().apply {
    runBlocking {
        if (getAllBookTags().isEmpty()) {
        }
    }
}
