package com.nw.persistence

import com.nw.models.BookTag

interface BookTagFacade {
    suspend fun getAllBookTags(): List<BookTag>
    suspend fun findAllBookTagsByBookId(bookId: Int): List<BookTag>
    suspend fun findAllBookTagsByTagId(tagId: Int): List<BookTag>
    suspend fun addTagToBook(bookId: Int, tagName: String, userId: Int): BookTag?
    suspend fun deleteBookTagByBookIdAndTagIdAndUserId(bookId: Int, tagId: Int, userId: Int): Boolean
    suspend fun deleteAllBookTagsByBookIdAndUserId(bookId: Int, userId: Int): Boolean
    suspend fun deleteBookTagById(id: Int): Boolean
    suspend fun countBookTagsByTagId(tagId: Int, userId: Int): Long
}
