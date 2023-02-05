package com.nw.persistence

import com.nw.models.BookTag

interface BookTagFacade {
    suspend fun getAllBookTags(): List<BookTag>
    suspend fun findAllBookTagsByBookId(bookId: Int): List<BookTag>
    suspend fun addTagToBook(bookId: Int, tagName: String): BookTag?
}
