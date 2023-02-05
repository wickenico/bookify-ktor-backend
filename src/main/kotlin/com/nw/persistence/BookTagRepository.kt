package com.nw.persistence

import com.nw.models.BookTag
import com.nw.models.BookTags
import com.nw.models.Tag
import com.nw.models.Tags
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class BookTagRepository : BookTagFacade {

    private fun resultRowToBookTag(row: ResultRow) = BookTag(
        bookId = row[BookTags.bookId],
        tagId = row[BookTags.tagId],
    )

    private fun resultRowToTag(row: ResultRow) = Tag(
        id = row[Tags.id],
        name = row[Tags.name]
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

        val insertStatement = BookTags.insert {
            it[BookTags.bookId] = bookId
            it[BookTags.tagId] = tagId
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBookTag)
    }

    override suspend fun findAllBookTagsByBookId(bookId: Int): List<BookTag> = DatabaseFactory.dbQuery {
        BookTags.select { BookTags.bookId eq bookId }
            .map(::resultRowToBookTag)
    }
}

val bookTagFacade: BookTagFacade = BookTagRepository().apply {
    runBlocking {
        if (getAllBookTags().isEmpty()) {
        }
    }
}
